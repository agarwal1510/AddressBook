package com.agarwalaman.addressbook.dao;

import com.agarwalaman.addressbook.entity.Contact;
import com.agarwalaman.addressbook.utils.Constants;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data access object class which interacts with the database.
 * Contains method to perform CRUD operations on the database.
 */
public class ContactDao {

    private Client client;
    private Gson gson;

    public ContactDao(Client client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    /**
     * Adds the contact to the database
     *
     * @param contact
     * @return true if successful else false
     */
    public boolean addContact(Contact contact) {
        IndexResponse response = client.prepareIndex(Constants.index, Constants.type)
                .setSource(gson.toJson(contact), XContentType.JSON).get();
        if (response != null && response.getId() != null) {
            return true;
        }
        return false;
    }

    /**
     * Fetches contact from database given a name
     *
     * @param name
     * @return Contact object if found else null
     */
    public Contact getContact(String name) {
        QueryBuilder query = QueryBuilders
                .matchQuery("name", name);
        SearchResponse response = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)                 // Query
                .get();
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                Map<String, Object> attrMap = searchHit.getSourceAsMap();
                Contact contact = new Contact(attrMap.get("name").toString(), attrMap.get("number").toString());
                return contact;
            }
        }
        return null;
    }

    /**
     * Returns a list of contacts from the database based on the given params
     *
     * @param pageSize
     * @param page
     * @param query
     * @return list of contacts found satisfying the conditions
     */
    public List<Contact> getContacts(int pageSize, int page, String query) {
        List<Contact> contactList = new ArrayList<>();
        Map<String, Object> attrMap;

        SearchRequestBuilder requestBuilder = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setFrom((page - 1) * pageSize)
                .setSize(pageSize);
        if (query != null && !query.isEmpty()) {
            requestBuilder.setQuery(QueryBuilders.queryStringQuery(query));
        }
        SearchResponse response = requestBuilder.get();
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                attrMap = searchHit.getSourceAsMap();
                contactList.add(new Contact(attrMap.get("name").toString(), attrMap.get("number").toString()));
            }
        }
        return contactList;
    }

    /**
     * Updates the contact in the database if matched with the given name
     *
     * @param name
     * @param contact Updated contact
     * @return true if successful else false
     */
    public boolean updateContact(String name, Contact contact) {
        String contactId = getContactId(name);
        if (contactId != null) {
            UpdateRequest request = new UpdateRequest();
            request.index(Constants.index);
            request.type(Constants.type);
            request.id(contactId);
            request.doc(gson.toJson(contact), XContentType.JSON);
            request.docAsUpsert(false);
            client.update(request);
            return true;
        }
        return false;
    }

    /**
     * Deletes a contact from the database
     *
     * @param name
     * @return true if successful else false
     */
    public boolean removeContact(String name) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("name", name))
                .source(Constants.index)
                .get();
        long deleted = response.getDeleted();
        return deleted > 0 ? true : false;
    }

    /**
     * Helper function to fetch a contact ID from the database
     *
     * @param name
     * @return contact id if found else null
     */
    public String getContactId(String name) {
        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery("name", name);
        SearchResponse response = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchSpecificFieldQuery)
                .get();
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                return searchHit.getId();
            }
        }
        return null;
    }

}

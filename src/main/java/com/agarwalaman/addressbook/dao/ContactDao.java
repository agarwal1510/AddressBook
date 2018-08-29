package com.agarwalaman.addressbook.dao;

import com.agarwalaman.addressbook.entity.Contact;
import com.agarwalaman.addressbook.utils.Constants;
import com.google.gson.Gson;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ContactDao {

    private Client client;
    private Gson gson;

    public ContactDao(Client client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public boolean addContact(Contact contact) {
        System.out.print("Trying to create index");
        IndexResponse response = client.prepareIndex(Constants.index, Constants.type)
                .setSource(gson.toJson(contact), XContentType.JSON).get();
        System.out.print(response.getId() + " " + response.getIndex() + " " + response.getType());
        return false;
    }

    public Contact getContact(String name) {
        QueryBuilder query = QueryBuilders
                .matchQuery("name", name);
        SearchResponse response = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)                 // Query
                .get();
        System.out.println(name + " " + response.toString());
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                Map<String, Object> attrMap = searchHit.getSourceAsMap();
                Contact contact = new Contact(attrMap.get("name").toString(), attrMap.get("number").toString());
                return contact;
            }
        }
        return null;
    }

    public List<Contact> getContacts(int pageSize, int page, String query) {
        List<Contact> contactList = new ArrayList<>();
        Map<String, Object> attrMap;

        SearchRequestBuilder requestBuilder = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setFrom((page-1) * pageSize)
                .setSize(pageSize);
        if (query != null && !query.isEmpty()) {
            requestBuilder.setQuery(QueryBuilders.queryStringQuery(query));
        }
        SearchResponse response = requestBuilder.get();
        System.out.println(response.toString());
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                attrMap = searchHit.getSourceAsMap();
                contactList.add(new Contact(attrMap.get("name").toString(), attrMap.get("number").toString()));
            }
        }
        return contactList;
    }

    public boolean updateContact(String name, Contact contact) {
        String contactId = getContactId(name);
        if (contactId != null) {
            try {
                UpdateRequest request = new UpdateRequest();
                request.index(Constants.index);
                request.type(Constants.type);
                request.id(contactId);
                request.doc(gson.toJson(contact), XContentType.JSON);
                request.docAsUpsert(false);
                ActionFuture<UpdateResponse> response = client.update(request);
                //response.get()

                System.out.println(response.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeContact(String name) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("name", name))
                .source(Constants.index)
                .get();
        long deleted = response.getDeleted();
        System.out.println("Deleted: " + deleted + " " + response.toString());
        return deleted > 0 ? true : false;
    }

    public String getContactId(String name) {
        QueryBuilder matchSpecificFieldQuery = QueryBuilders
                .matchQuery("name", name);
        SearchResponse response = client.prepareSearch(Constants.index)
                .setTypes(Constants.type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchSpecificFieldQuery)
                .get();
        System.out.println(name + " " + response.toString());
        if (response.getHits().totalHits > 0) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                return searchHit.getId();
            }
        }
        return null;
    }

}

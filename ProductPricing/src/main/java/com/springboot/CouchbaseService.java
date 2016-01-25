package com.springboot;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.AsyncViewResult;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.springboot.config.Database;

import rx.Observable;


@Service
public class CouchbaseService {

    private final Database config;

    private final Bucket bucket;
    private final Cluster cluster;

    @Autowired
    public CouchbaseService(final Database config) {
        this.config = config;

        //connect to the cluster and open the configured bucket
        this.cluster = CouchbaseCluster.create(config.getNodes());
        this.bucket = cluster.openBucket(config.getBucket(), config.getPassword());
    }

    @PreDestroy
    public void preDestroy() {
        if (this.cluster != null) {
            this.cluster.disconnect();
        }
    }

    /**
     * Prepare a new JsonDocument with some JSON content
     */
    public static JsonDocument createDocument(String id, JsonObject content) {
        return JsonDocument.create(id, content);
    }

    /**
     * CREATE the document in database
     * @return the created document, with up to date metadata
     */
    public JsonDocument create(JsonDocument doc) {
        return bucket.insert(doc);
    }

    /**
     * READ the document from database
     */
    public JsonDocument read(String id) {
        return bucket.get(id);
    }

    /**
     * UPDATE the document in database
     * @return the updated document, with up to date metadata
     */
    public JsonDocument update(JsonDocument doc) {
        return bucket.replace(doc);
    }

    /**
     * DELETE the document from database
     * @return the deleted document, with only metadata (since content has been deleted)
     */
    public JsonDocument delete(String id) {
        return bucket.remove(id);
    }

    /**
     * Uses a view query to find all beers. Possibly use an offset and a limit of the
     * number of beers to retrieve.
     *
     * @param offset the number of beers to skip, null or < 1 to ignore
     * @param limit the limit of beers to retrieve, null or < 1 to ignore
     */
    public ViewResult findAllBeers(Integer offset, Integer limit) {
        ViewQuery query = ViewQuery.from("dev_product_price", "get_all_prices");
        if (limit != null && limit > 0) {
            query.limit(limit);
        }
        if (offset != null && offset > 0) {
            query.skip(offset);
        }
        ViewResult result = bucket.query(query);
        return result;
    }

    /**
     * Retrieves all the beers using a view query, returning the result asynchronously.
     */
    public Observable<AsyncViewResult> findAllBeersAsync() {
        ViewQuery allBeers = ViewQuery.from("dev_product_price", "get_all_prices");
        return bucket.async().query(allBeers);
    }

    /**
     * READ the document asynchronously from database.
     */
    public Observable<JsonDocument> asyncRead(String id) {
        return bucket.async().get(id);
    }

}

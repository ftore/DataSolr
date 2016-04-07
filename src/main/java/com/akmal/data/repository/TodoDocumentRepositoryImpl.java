package com.akmal.data.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.stereotype.Repository;

import com.akmal.data.document.TodoDocument;

import javax.annotation.Resource;

@Repository
public class TodoDocumentRepositoryImpl implements PartialUpdateRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoDocumentRepositoryImpl.class);

    @Resource
    private SolrTemplate solrTemplate;

    @Override
    public void update(TodoDocument todoEntry) {
        LOGGER.debug("Performing partial update for todo entry: {}", todoEntry);

        PartialUpdate update = new PartialUpdate(TodoDocument.FIELD_ID, todoEntry.getId().toString());

        update.add(TodoDocument.FIELD_DESCRIPTION, todoEntry.getDescription());
        update.add(TodoDocument.FIELD_TITLE, todoEntry.getTitle());

        solrTemplate.saveBean(update);
        solrTemplate.commit();
    }
}

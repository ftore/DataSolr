package com.akmal.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akmal.data.document.TodoDocument;
import com.akmal.data.repository.TodoDocumentRepository;

@Service("todoIndexService")
public class TodoIndexServiceImpl implements TodoIndexService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TodoIndexServiceImpl.class);

    protected static final String QUERY_METHOD_METHOD_NAME = "methodName";
    protected static final String QUERY_METHOD_NAMED_QUERY = "namedQuery";
    protected static final String QUERY_METHOD_QUERY_ANNOTATION = "queryAnnotation";
	
	@Resource
	private TodoDocumentRepository repository;
	
	@Value("${solr.repository.query.method.type}")
    private String queryMethodType;

    @Transactional
    @Override
    public void addToIndex(TodoDocument todoEntry) {
    	repository.save(todoEntry);
    }
    
    @Transactional
    @Override
    public void deleteFromIndex(Long id) {
    	repository.delete(id.toString());
    }
    
    @Transactional
    @Override
    public List<TodoDocument> search(String searchTerm) {
    	LOGGER.debug("Searching documents with search term: {}", searchTerm);
        return findDocuments(searchTerm);
    }
    
    @Transactional
    @Override
    public void update(TodoDocument todoEntry) {
    	LOGGER.debug("Updating the information of a todo entry: {}", todoEntry);
    	repository.update(todoEntry);
    }
    
    private List<TodoDocument> findDocuments(String searchTerm) {
        if (queryMethodType != null) {
            if (queryMethodType.equals(QUERY_METHOD_METHOD_NAME)) {
                LOGGER.debug("Finding todo entries by using query generation from method name.");
                return repository.findByTitleContainsOrDescriptionContains(searchTerm, searchTerm, sortByIdDesc());
            }
            else if (queryMethodType.equals(QUERY_METHOD_NAMED_QUERY)) {
                LOGGER.debug("Finding todo entries by using named queries.");
                return repository.findByNamedQuery(searchTerm, sortByIdDesc());
            }
            else if (queryMethodType.equals(QUERY_METHOD_QUERY_ANNOTATION)) {
                LOGGER.debug("Finding todo entries by using @Query annotation.");
                return repository.findByQueryAnnotation(searchTerm, sortByIdDesc());
            }
        }

        LOGGER.debug("Unknown query method type: {}. Returning empty list.", queryMethodType);
        return new ArrayList<TodoDocument>();
    }

    private Sort sortByIdDesc() {
        return new Sort(Sort.Direction.DESC, TodoDocument.FIELD_ID);
    }
}

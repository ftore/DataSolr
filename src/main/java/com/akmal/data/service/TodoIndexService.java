package com.akmal.data.service;

import java.util.List;

import com.akmal.data.document.TodoDocument;

/**
 * @author Petri Kainulainen
 */
public interface TodoIndexService {

    public void addToIndex(TodoDocument todoEntry);

    public void deleteFromIndex(Long id);

    public List<TodoDocument> search(String searchTerm);

    public void update(TodoDocument todoEntry);
}

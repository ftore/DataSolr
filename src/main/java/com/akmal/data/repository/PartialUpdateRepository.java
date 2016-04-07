package com.akmal.data.repository;

import com.akmal.data.document.TodoDocument;

public interface PartialUpdateRepository {
	public void update(TodoDocument todoEntry);
}

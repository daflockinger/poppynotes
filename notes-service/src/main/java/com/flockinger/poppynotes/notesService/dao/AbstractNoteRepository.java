package com.flockinger.poppynotes.notesService.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public abstract class AbstractNoteRepository<T> {
	private final static int MAX_TIMEOUT_MILSEC = 3000;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * Sorts first by pinned (true first) and secondary by lastEdit date descending
	 * (new date first) and returns all entries of a user, limited by page-size, 
	 * and started by the page-offset.
	 * 
	 * @param userId
	 * @param pageable
	 * @return
	 */
	abstract List<T> findByUserIdSorted(Long userId, Pageable pageable);

	/**
	 * Sorts first by pinned (true first) and secondary by lastEdit date descending
	 * (new date first) and returns all notes like entities of a user, limited by page-size, 
	 * and started by the page-offset.
	 * 
	 * @param userId
	 * @param noteClass
	 * @param pageable
	 * @return
	 */
	protected List<T> findByUserIdSortedFromAnyNote(Long userId, Pageable pageable, Class<T> noteClass) {

		Query findUserNotesSorted = new Query();
		findUserNotesSorted.addCriteria(where("userId").is(userId)).slaveOk().maxTimeMsec(MAX_TIMEOUT_MILSEC)
				.limit(pageable.getPageSize()).skip(pageable.getOffset())
				.with(new Sort(Direction.DESC, "pinned", "lastEdit"));

		List<T> results = mongoTemplate.find(findUserNotesSorted, noteClass);

		return results != null ? results : new ArrayList<>();
	}
}

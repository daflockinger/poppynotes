package com.flockinger.poppynotes.notesService.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.poppynotes.notesService.BaseDataBaseTest;
import com.flockinger.poppynotes.notesService.TestDataFactory;
import com.flockinger.poppynotes.notesService.model.Note;
import com.google.common.collect.ImmutableList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteRepositoryTest extends BaseDataBaseTest {

	@Test
	public void testFindByUserIdSorted_withValidQuery_shouldReturnCorrectSortedByPinnedAndLastEditedDescending() {

		List<Note> firstFive = dao.findByUserIdSorted(1l, new PageRequest(0, 5));

		assertEquals("check latest pinned", "1-pinned-latest", firstFive.get(0).getTitle());
		assertEquals("check second pinned", "2-pinned-second", firstFive.get(1).getTitle());
		assertEquals("check third pinned", "3-pinned-third", firstFive.get(2).getTitle());
		assertEquals("check real latest non-pinned", "1latest", firstFive.get(3).getTitle());
		assertEquals("check second non-pinned", "2second", firstFive.get(4).getTitle());

		List<Note> secondFive = dao.findByUserIdSorted(1l, new PageRequest(1, 5));

		assertEquals("check third", "3thrid", secondFive.get(0).getTitle());
		assertEquals("check fourth", "4fourth", secondFive.get(1).getTitle());
		assertEquals("check fifth", "5fifth", secondFive.get(2).getTitle());
		assertEquals("check second oldest", "second oldest", secondFive.get(3).getTitle());
		assertEquals("check oldest",
				"oldest-non-pinned. Capicola kevin ham hock, pancetta ribeye beef ribs short loin pork loin sirloin tail pig porchetta boudin. Chuck porchetta meatloaf, beef ham swine tenderloin tail.",
				secondFive.get(4).getTitle());

	}
	
	@Test
	public void testFindByUserIdSorted_withUserWithNoNotes_shouldReturnEmpty() {
		List<Note> result = dao.findByUserIdSorted(2l, new PageRequest(0, 5));
		assertNotNull(result);
	}
}

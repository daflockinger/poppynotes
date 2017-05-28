package com.flockinger.poppynotes.notesService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flockinger.poppynotes.notesService.model.ArchivedNote;
import com.flockinger.poppynotes.notesService.model.Note;
import com.google.common.collect.ImmutableList;

public class TestDataFactory {
	
	public static List<Note> getSimpleTestNotes() {
		Note note1 = new Note();
		note1.setUserId(1l);
		note1.setLastEdit(new Date(100));
		note1.setTitle(
				"oldest-non-pinned. Capicola kevin ham hock, pancetta ribeye beef ribs short loin pork loin sirloin tail pig porchetta boudin. Chuck porchetta meatloaf, beef ham swine tenderloin tail.");
		note1.setContent(
				"Tongue t-bone prosciutto pastrami shank biltong venison ham jowl filet mignon bacon jerky porchetta strip steak. Meatloaf tenderloin meatball boudin t-bone. Rump salami ham venison pork chop shankle sausage tri-tip prosciutto beef turducken ball tip ham hock strip steak pork belly. Shankle kielbasa sausage filet mignon.");
		note1.setPinned(false);

		Note note2 = new Note();
		note2.setUserId(1l);
		note2.setLastEdit(new Date(300));
		note2.setTitle("second oldest");
		note2.setContent("Ribeye doner sausage flank pastrami tri-tip cupim. ");
		note2.setPinned(false);

		Note note3 = new Note();
		note3.setUserId(1l);
		note3.setLastEdit(new Date(200));
		note3.setTitle("3-pinned-third");
		note3.setContent(
				"Turkey chuck jerky, pork chop short loin rump sausage boudin alcatra bresaola meatball flank.");
		note3.setPinned(true);

		Note note4 = new Note();
		note4.setUserId(1l);
		note4.setLastEdit(new Date(500));
		note4.setTitle("5fifth");
		note4.setContent(
				"Cupim beef ribs hamburger, flank ball tip strip steak tongue rump tri-tip kielbasa ribeye doner capicola. ");
		note4.setPinned(false);

		Note note5 = new Note();
		note5.setUserId(1l);
		note5.setLastEdit(new Date(1000));
		note5.setTitle("4fourth");
		note5.setContent("Meatloaf shank short loin, beef ribs venison doner shoulder pork chop short ribs.");
		note5.setPinned(false);

		Note note6 = new Note();
		note6.setUserId(1l);
		note6.setLastEdit(new Date(3000));
		note6.setTitle("2-pinned-second");
		note6.setContent(
				"bresaola burgdoggen landjaeger tenderloin hamburger shoulder tail. Corned beef ground round tri-tip kevin fatback brisket drumstick beef ribs leberkas shank.");
		note6.setPinned(true);

		Note note7 = new Note();
		note7.setUserId(1l);
		note7.setLastEdit(new Date(5000));
		note7.setTitle("3thrid");
		note7.setContent(
				"Spicy jalapeno bacon ipsum dolor amet ball tip landjaeger pastrami turkey tail meatball swine corned beef beef ribs jowl brisket cupim ground round kevin ham hock. ");
		note7.setPinned(false);

		Note note8 = new Note();
		note8.setUserId(1l);
		note8.setLastEdit(new Date(7000));
		note8.setTitle("2second");
		note8.setContent("Bacon boudin spare ribs sausage rump corned beef alcatra drumstick frankfurter tri-tip");
		note8.setPinned(false);

		Note note9 = new Note();
		note9.setUserId(1l);
		note9.setLastEdit(new Date(9000));
		note9.setTitle("1-pinned-latest");
		note9.setPinned(true);
		note9.setContent(
				"Tenderloin frankfurter bacon turducken spare ribs drumstick landjaeger pork loin kevin jowl pastrami salami cupim. Turducken bacon chuck, spare ribs burgdoggen andouille pig short loin kevin short ribs brisket.");

		Note note10 = new Note();
		note10.setId("existingNoteId");
		note10.setUserId(1l);
		note10.setLastEdit(new Date(10000));
		note10.setTitle("1latest");
		note10.setContent(
				"Beef ribs meatloaf fatback pork belly ball tip burgdoggen, tail ribeye salami chicken tongue tenderloin drumstick ");
		note10.setPinned(false);

		List<Note> notes = new ArrayList<>();
		notes.addAll(ImmutableList.of(note1, note2, note3, note4, note5));
		notes.addAll(ImmutableList.of(note6, note7, note8, note9, note10));

		return notes;
	}
	
	public static List<ArchivedNote> getSimpleTestArchivedNotes() {
		ArchivedNote note1 = new ArchivedNote();
		note1.setUserId(1l);
		note1.setLastEdit(new Date(100));
		note1.setTitle(
				"aoldest-non-pinned. Capicola kevin ham hock, pancetta ribeye beef ribs short loin pork loin sirloin tail pig porchetta boudin. Chuck porchetta meatloaf, beef ham swine tenderloin tail.");
		note1.setContent(
				"archived - Tongue t-bone prosciutto pastrami shank biltong venison ham jowl filet mignon bacon jerky porchetta strip steak. Meatloaf tenderloin meatball boudin t-bone. Rump salami ham venison pork chop shankle sausage tri-tip prosciutto beef turducken ball tip ham hock strip steak pork belly. Shankle kielbasa sausage filet mignon.");
		note1.setPinned(false);

		ArchivedNote note2 = new ArchivedNote();
		note2.setUserId(1l);
		note2.setLastEdit(new Date(300));
		note2.setTitle("asecond oldest");
		note2.setContent("archived - Ribeye doner sausage flank pastrami tri-tip cupim. ");
		note2.setPinned(false);

		ArchivedNote note3 = new ArchivedNote();
		note3.setUserId(1l);
		note3.setLastEdit(new Date(200));
		note3.setTitle("a3-pinned-third");
		note3.setContent(
				"archived - Turkey chuck jerky, pork chop short loin rump sausage boudin alcatra bresaola meatball flank.");
		note3.setPinned(true);

		ArchivedNote note4 = new ArchivedNote();
		note4.setUserId(1l);
		note4.setLastEdit(new Date(500));
		note4.setTitle("a5fifth");
		note4.setContent(
				"archived - Cupim beef ribs hamburger, flank ball tip strip steak tongue rump tri-tip kielbasa ribeye doner capicola. ");
		note4.setPinned(false);

		ArchivedNote note5 = new ArchivedNote();
		note5.setUserId(1l);
		note5.setLastEdit(new Date(1000));
		note5.setTitle("a4fourth");
		note5.setContent("archived - Meatloaf shank short loin, beef ribs venison doner shoulder pork chop short ribs.");
		note5.setPinned(false);

		ArchivedNote note6 = new ArchivedNote();
		note6.setUserId(1l);
		note6.setLastEdit(new Date(3000));
		note6.setTitle("a2-pinned-second");
		note6.setContent(
				"archived - bresaola burgdoggen landjaeger tenderloin hamburger shoulder tail. Corned beef ground round tri-tip kevin fatback brisket drumstick beef ribs leberkas shank.");
		note6.setPinned(true);

		ArchivedNote note7 = new ArchivedNote();
		note7.setUserId(1l);
		note7.setLastEdit(new Date(5000));
		note7.setTitle("a3thrid");
		note7.setContent(
				"archived - Spicy jalapeno bacon ipsum dolor amet ball tip landjaeger pastrami turkey tail meatball swine corned beef beef ribs jowl brisket cupim ground round kevin ham hock. ");
		note7.setPinned(false);

		ArchivedNote note8 = new ArchivedNote();
		note8.setUserId(1l);
		note8.setLastEdit(new Date(7000));
		note8.setTitle("a2second");
		note8.setContent("archived - Bacon boudin spare ribs sausage rump corned beef alcatra drumstick frankfurter tri-tip");
		note8.setPinned(false);

		ArchivedNote note9 = new ArchivedNote();
		note9.setUserId(1l);
		note9.setLastEdit(new Date(9000));
		note9.setTitle("a1-pinned-latest");
		note9.setPinned(true);
		note9.setContent(
				"archived - Tenderloin frankfurter bacon turducken spare ribs drumstick landjaeger pork loin kevin jowl pastrami salami cupim. Turducken bacon chuck, spare ribs burgdoggen andouille pig short loin kevin short ribs brisket.");

		ArchivedNote note10 = new ArchivedNote();
		note10.setId("aexistingNoteId");
		note10.setUserId(1l);
		note10.setLastEdit(new Date(10000));
		note10.setTitle("a1latest");
		note10.setContent(
				"archived - Beef ribs meatloaf fatback pork belly ball tip burgdoggen, tail ribeye salami chicken tongue tenderloin drumstick ");
		note10.setPinned(false);

		List<ArchivedNote> notes = new ArrayList<>();
		notes.addAll(ImmutableList.of(note1, note2, note3, note4, note5));
		notes.addAll(ImmutableList.of(note6, note7, note8, note9, note10));

		return notes;
	}
}

package edu.studyup.serviceImpl;



import static org.junit.jupiter.api.Assertions.*;



import java.util.ArrayList;

import java.util.Date;

import java.util.List;



import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;



import edu.studyup.entity.Event;

import edu.studyup.entity.Location;

import edu.studyup.entity.Student;

import edu.studyup.util.DataStorage;

import edu.studyup.util.StudyUpException;



class EventServiceImplTest {



	EventServiceImpl eventServiceImpl;



	@BeforeAll

	static void setUpBeforeClass() throws Exception {

	}



	@AfterAll

	static void tearDownAfterClass() throws Exception {

	}



	@BeforeEach

	void setUp() throws Exception {

		eventServiceImpl = new EventServiceImpl();

		//Create Student

		Student student = new Student();

		student.setFirstName("John");

		student.setLastName("Doe");

		student.setEmail("JohnDoe@email.com");

		student.setId(1);

		

		//Create Event1

		Event event = new Event();

		event.setEventID(1);

		event.setDate(new Date());

		event.setName("Event 1");

		Location location = new Location(-122, 37);

		event.setLocation(location);

		List<Student> eventStudents = new ArrayList<>();

		eventStudents.add(student);

		event.setStudents(eventStudents);

		DataStorage.eventData.put(event.getEventID(), event);

	}


	@AfterEach

	void tearDown() throws Exception {

		DataStorage.eventData.clear();

	}

	@Test
	void testUpdateEventName_less_than_20_GoodCase() throws StudyUpException {
	
		int eventID = 1;
		
		eventServiceImpl.updateEventName(eventID, "abcabcabca");

	}


	@Test
	void testGetPastEvents() {
	
		
		Student student4 = new Student();

		student4.setFirstName("f4");

		student4.setLastName("l4");

		student4.setEmail("f3=4l4@email.com");

		student4.setId(3);
		
		
		Event event3 = new Event();

		event3.setEventID(3);
		@SuppressWarnings("deprecation")
		Date future = new Date(99, 01,01);

		event3.setDate(future);

		event3.setName("Event 3");

		Location location = new Location(-122, 37);

		event3.setLocation(location);

		List<Student> eventStudents = new ArrayList<>();

		eventStudents.add(student4);

		event3.setStudents(eventStudents);

		DataStorage.eventData.put(event3.getEventID(), event3);
		
		eventServiceImpl.getPastEvents();
		
	}
	
	
	// test should give no errors when name.length = 20
	@Test
	void testUpdateEventName_exactly20_GoodCase() throws StudyUpException {
	
		int eventID = 1;
		
		eventServiceImpl.updateEventName(eventID, "abcabcabcabcabcabcab");
		
		Assertions.assertThrows(StudyUpException.class, () -> {

			eventServiceImpl.updateEventName(eventID, "abcabcabcabcabcabcab");

		  });
	}
	
	
	@Test
	void testAddStudentToEvent_over_2_BadCase() throws StudyUpException {
		
		//Create Students

		Student student1 = new Student();

		student1.setFirstName("f1");

		student1.setLastName("l1");

		student1.setEmail("f1l1@email.com");

		student1.setId(1);
		
		
		
		Student student2 = new Student();

		student2.setFirstName("f2");

		student2.setLastName("l2");

		student2.setEmail("f2l2@email.com");

		student2.setId(2);
	
		
		Student student3 = new Student();

		student3.setFirstName("f3");

		student3.setLastName("l3");

		student3.setEmail("f3l3@email.com");

		student3.setId(3);
		
		// create event 2
		Event event2 = new Event();

		event2.setEventID(2);

		event2.setDate(new Date());

		event2.setName("Event 2");

		Location location = new Location(-122, 37);

		event2.setLocation(location);

		List<Student> eventStudents = new ArrayList<>();

		eventStudents.add(student1);

		event2.setStudents(eventStudents);

		DataStorage.eventData.put(event2.getEventID(), event2);
		
		eventServiceImpl.addStudentToEvent(student1, 2);
		eventServiceImpl.addStudentToEvent(student2, 2);
		eventServiceImpl.addStudentToEvent(student3, 2);
		
		assertTrue(event2.getStudents().size() <= 2, "Failure, there are more than two students at an event" );
		
	}
	
	@Test
	void testAddStudentToEvent_null_event_BadCase() throws StudyUpException {
		
		//Create Students

		Student student1 = new Student();

		student1.setFirstName("f1");

		student1.setLastName("l1");

		student1.setEmail("f1l1@email.com");

		student1.setId(1);
		
		Assertions.assertThrows(StudyUpException.class, () -> {	
			eventServiceImpl.addStudentToEvent(student1, 3);
		  });	
	}
	
	
	@Test
	void testAddStudentToEvent_no_students_BadCase() throws StudyUpException {
		
		//Create Students

		Student student1 = null;
		
		Event event2 = new Event();

		event2.setEventID(2);
		
		List<Student> eventStudents = null;
		
		event2.setStudents(eventStudents);
		DataStorage.eventData.put(event2.getEventID(), event2);

		eventServiceImpl.addStudentToEvent(student1,2);
	}
	
	@Test
	void testGetActiveEvents_BadCase() {
		
		// create student
		
		Student student4 = new Student();

		student4.setFirstName("f4");

		student4.setLastName("l4");

		student4.setEmail("f3=4l4@email.com");

		student4.setId(3);
		
		// create event3
		
		Event event3 = new Event();

		event3.setEventID(3);
		@SuppressWarnings("deprecation")
		Date future = new Date(100, 01,01);

		event3.setDate(future);

		event3.setName("Event 3");

		Location location = new Location(-122, 37);

		event3.setLocation(location);

		List<Student> eventStudents = new ArrayList<>();

		eventStudents.add(student4);

		event3.setStudents(eventStudents);

		DataStorage.eventData.put(event3.getEventID(), event3);
		
		assertTrue(eventServiceImpl.getActiveEvents().size() == 0, "False, it holds older dates");
	}
	
	@Test
	void testUpdateEventName_over20_BadCase() throws StudyUpException {
		
		int eventID = 1;
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "abcabcabcabcabcabcabcdefghi");
		  });
	}
	
	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {

		int eventID = 1;

		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");

		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());

	}
	
	@Test
	void testDeleteEvent() {
	
		Event event3 = new Event();

		event3.setEventID(5);
		@SuppressWarnings("deprecation")
		Date future = new Date(100, 01,01);

		event3.setDate(future);

		event3.setName("Event 3");

		Location location = new Location(-122, 37);

		event3.setLocation(location);

		List<Student> eventStudents = new ArrayList<>();

		event3.setStudents(eventStudents);

		DataStorage.eventData.put(event3.getEventID(), event3);
			
		eventServiceImpl.deleteEvent(event3.getEventID());
		
	}
//
	@Test
	void testUpdateEvent_WrongEventID_badCase() {

		int eventID = 3;

		Assertions.assertThrows(StudyUpException.class, () -> {

			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");

		  });
	}
}
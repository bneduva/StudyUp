package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateEventName_LongEventName_BadCase(){
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 1 - Long Name");
		});
	}

	@Test
	void testUpdateEventName_MaxLengthName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1 long");
		assertEquals("Renamed Event 1 long", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testGetActiveEvents_FutureEventDate_GoodCase(){
		int eventID = 1;
		Date futureDate = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
		DataStorage.eventData.get(eventID).setDate(futureDate);
		
		List<Event> result = eventServiceImpl.getActiveEvents();
		
		assertEquals(eventID, result.get(0).getEventID());
	}
	
	@Test
	void testGetActiveEvents_PastEventDate_BadCase(){
		int eventID = 1;
		Date futureDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
		DataStorage.eventData.get(eventID).setDate(futureDate);
		
		List<Event> result = eventServiceImpl.getActiveEvents();
		
		assertEquals(0, result.size());
	}
	
	@Test
	void testGetPastEvents_PastEventDate_GoodCase(){
		int eventID = 1;
		Date pastDate = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
		DataStorage.eventData.get(eventID).setDate(pastDate);
		
		List<Event> result = eventServiceImpl.getPastEvents();
		assertEquals(eventID, result.get(0).getEventID());
	}
	
	@Test
	void testGetPastEvents_FutureEventDate_BadCase(){
		int eventID = 1;
		Date futureDate = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
		DataStorage.eventData.get(eventID).setDate(futureDate);
		
		List<Event> result = eventServiceImpl.getPastEvents();
		
		assertEquals(0, result.size());
	}
	
	@Test
	void testAddStudentToEvent_StudentInEvent_GoodCase() throws StudyUpException {
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("Jane");
		student.setLastName("Doe");
		student.setEmail("JaneDoe@email.com");
		student.setId(2);
		
		List<Student> expected = new ArrayList<>();
		expected.add(DataStorage.eventData.get(eventID).getStudents().get(0));
		expected.add(student);
		
		Event resultEvent = eventServiceImpl.addStudentToEvent(student, eventID);
		
		List<Student> resultStudents = resultEvent.getStudents();
		
		assertEquals(expected, resultStudents);
	}
	
	@Test
	void testAddStudentToEvent_NoStudentsInEvent_GoodCase() throws StudyUpException {
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("Jane");
		student.setLastName("Doe");
		student.setEmail("JaneDoe@email.com");
		student.setId(2);
		
		List<Student> expected = new ArrayList<>();
		expected.add(student);

		DataStorage.eventData.get(eventID).setStudents(null);
		
		Event resultEvent = eventServiceImpl.addStudentToEvent(student, eventID);
		
		List<Student> resultStudents = resultEvent.getStudents();
		
		assertEquals(expected, resultStudents);
	}
	
	@Test
	void testAddStudentToEvent_WrongEventID_BadCase(){
		int eventID = 3;
		Student student = new Student();
		student.setFirstName("Jane");
		student.setLastName("Doe");
		student.setEmail("JaneDoe@email.com");
		student.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student, eventID);
		});
	}
	
	@Test
	void testAddStudentToEvent_TooManyStudents_BadCase() throws StudyUpException {
		int eventID = 1;
		Student student1 = new Student();
		Student student2 = new Student();
		Student student3 = new Student();
		student1.setFirstName("Jane");
		student2.setFirstName("Frodo");
		student3.setFirstName("Samwise");
		student1.setLastName("Doe");
		student2.setLastName("Baggins");
		student3.setLastName("Gamgee");
		student1.setEmail("JaneDoe@email.com");
		student2.setEmail("FrodoBaggins@email.com");
		student3.setEmail("SamwiseGamgee@email.com");
		student1.setId(1);
		student2.setId(2);
		student3.setId(3);
		
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student1);
		eventStudents.add(student2);
		
		DataStorage.eventData.get(eventID).setStudents(eventStudents);

		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student3, eventID);
		});
	}
	
	@Test
	void testDeleteEvent_GoodCase() throws StudyUpException {
		int eventID = 1;
		
		Event expected = DataStorage.eventData.get(eventID);
		
		Event result = eventServiceImpl.deleteEvent(eventID);
		assertEquals(expected, result);
	}
	
	@Test
	void testDeleteEvent_WrongEventID_BadCase() throws StudyUpException {
		int eventID = 3;
		
		Event result = eventServiceImpl.deleteEvent(eventID);
		assertEquals(null, result);
	}
}

package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Building;
import jumpstart.business.domain.workout.Room;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class CannotDeleteIsReferencedExceptionTest extends BaseTest {

	@Test
	public void test_CannotDeleteIsReferencedException_is_thrown_on_deleting_entity_that_is_referenced()
			throws BusinessException {

		// First make a building with a room and a teacher that references the room.

		Building building = new Building("Big");
		building = workoutService.createBuilding(building);

		Room room = new Room("West1", building);
		room = workoutService.addRoom(room, false);

		Teacher teacher = new Teacher("Mr Magoo", null, room);
		teacher = workoutService.createTeacher(teacher, false);

		// Try to remove the room - expect a CannotDeleteIsReferencedException
		// because the room is referenced by a teacher.

		room = workoutService.findRoom(room.getId());
		try {
			workoutService.removeRoom(room);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), CannotDeleteIsReferencedException.class, s.getClass());

			CannotDeleteIsReferencedException x = (CannotDeleteIsReferencedException) s;
			if (x.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_REFBYENTITY) {
				assertEquals("cannot delete because there are references to it or its parts by \"teacher\".", s
						.getMessage().toLowerCase());
			}
			else if (x.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_TABLE_ID) {
				assertEquals("Cannot delete \"" + room.getId()
						+ "\" from table ROOM because there are references to it.", s.getMessage());
			}

		}

	}

	@Test
	public void test_CannotDeleteIsReferencedException_is_thrown_on_deleting_whole_whose_parts_are_referenced()
			throws BusinessException {

		// First make a building with a room and a teacher that references the room.

		Building building = new Building("Big");
		building = workoutService.createBuilding(building);

		Room room = new Room("West1", building);
		room = workoutService.addRoom(room, false);

		Teacher teacher = new Teacher("Mr Magoo", null, room);
		teacher = workoutService.createTeacher(teacher, false);

		// Try to delete the building - expect a CannotDeleteIsReferencedException
		// because it has a room that's referenced by a teacher.

		try {
			building = workoutService.findBuilding(building.getId());
			workoutService.deleteBuilding(building);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = interpreter.interpret(e);
			assertEquals(s.getMessage(), CannotDeleteIsReferencedException.class, s.getClass());

			CannotDeleteIsReferencedException x = (CannotDeleteIsReferencedException) s;
			if (x.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_REFBYENTITY) {
				assertEquals("cannot delete because there are references to it or its parts by \"teacher\".", s
						.getMessage().toLowerCase());
			}
			else if (x.getInformationLevel() == CannotDeleteIsReferencedException.INFORMATIONLEVEL_TABLE_ID) {
				assertEquals("Cannot delete \"" + room.getId()
						+ "\" from table ROOM because there are references to it.", s
						.getMessage());
			}
		}

	}

}

package com.university.disasterresponsesystem.server.service;

import com.university.disasterresponsesystem.common.model.*;
import com.university.disasterresponsesystem.dao.IncidentDao;
import com.university.disasterresponsesystem.dao.ResourceDao;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Feature 2: Resource Allocation Tracker.
 *
 * @author Joyee Chakraborty - 12286715
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResourceServiceTest {

    private static class InMemoryResourceDao extends ResourceDao {

        private final java.util.List<Resource> store = new java.util.ArrayList<>();
        private long idSeq = 1;

        @Override
        public Resource insert(Resource r) {
            r.setId(idSeq++);
            store.add(r);
            return r;
        }

        @Override
        public java.util.List<Resource> findAll() {
            return new java.util.ArrayList<>(store);
        }

        @Override
        public Resource findById(Long id) {
            for (Resource r : store) {
                if (r.getId().equals(id)) return r;
            }
            return null;
        }

        @Override
        public void update(Resource r) {
            for (int i = 0; i < store.size(); i++) {
                if (store.get(i).getId().equals(r.getId())) {
                    store.set(i, r);
                    return;
                }
            }
        }
    }

    private static class InMemoryIncidentDao extends IncidentDao {
        @Override
        public Incident findById(Long id) {
            Incident stub = new Incident();
            stub.setId(id);
            return stub;
        }
    }

    private ResourceService service;

    @BeforeEach
    void setUp() {
        service = new ResourceService(new InMemoryResourceDao(), new InMemoryIncidentDao());
    }

    @Test
    @Order(1)
    @DisplayName("addResource - returns resource with generated ID")
    void testAddResourceReturnsId() {
        Resource r = service.addResource("Fire Truck 1", DepartmentType.FIRE_AND_EMERGENCY);
        assertNotNull(r, "Resource should not be null");
        assertNotNull(r.getId(), "Generated ID should not be null");
        assertEquals("Fire Truck 1", r.getName());
        assertEquals(DepartmentType.FIRE_AND_EMERGENCY, r.getDepartment());
    }

    @Test
    @Order(2)
    @DisplayName("addResource - default status is AVAILABLE")
    void testAddResourceDefaultStatus() {
        Resource r = service.addResource("Ambulance 3", DepartmentType.HOSPITAL);
        assertEquals(ResourceStatus.AVAILABLE, r.getStatus(),
                "Newly added resource should be AVAILABLE");
    }

    @Test
    @Order(3)
    @DisplayName("getResources - returns all added resources")
    void testGetResources() {
        service.addResource("Police Unit 1", DepartmentType.POLICE);
        service.addResource("Water Pump", DepartmentType.WATER_SUPPLY);
        List<Resource> all = service.getResources();
        assertEquals(2, all.size());
    }

    @Test
    @Order(4)
    @DisplayName("allocate - dispatches resource to incident")
    void testAllocate() {
        Resource r = service.addResource("Rescue Boat", DepartmentType.FIRE_AND_EMERGENCY);
        Resource allocated = service.allocate(r.getId(), 10L);
        assertEquals(ResourceStatus.DISPATCHED, allocated.getStatus());
        assertEquals(10L, allocated.getAssignedIncidentId());
    }

    @Test
    @Order(5)
    @DisplayName("allocate - throws if resource already dispatched")
    void testAllocateAlreadyDispatched() {
        Resource r = service.addResource("Chopper Med-1", DepartmentType.HOSPITAL);
        service.allocate(r.getId(), 1L);
        assertThrows(IllegalStateException.class,
                () -> service.allocate(r.getId(), 2L),
                "Should throw IllegalStateException when resource is not AVAILABLE");
    }

    @Test
    @Order(6)
    @DisplayName("allocate - throws if resource ID does not exist")
    void testAllocateUnknownResource() {
        assertThrows(IllegalArgumentException.class,
                () -> service.allocate(999L, 1L),
                "Should throw IllegalArgumentException for unknown resource ID");
    }

    @Test
    @Order(7)
    @DisplayName("allocate - throws if incident ID does not exist")
    void testAllocateUnknownIncident() {
        service = new ResourceService(new InMemoryResourceDao(), new IncidentDao() {
            @Override
            public Incident findById(Long id) { return null; }
        });
        Resource r = service.addResource("Power Team", DepartmentType.ELECTRICITY);
        assertThrows(IllegalArgumentException.class,
                () -> service.allocate(r.getId(), 999L),
                "Should throw IllegalArgumentException for unknown incident ID");
    }

    @Test
    @Order(8)
    @DisplayName("addResource - multiple departments can be added independently")
    void testMultipleDepartments() {
        service.addResource("Bus Fleet", DepartmentType.TRANSPORT);
        service.addResource("Shelter Team", DepartmentType.SHELTER_SERVICES);
        service.addResource("Waste Crew", DepartmentType.WASTE_MANAGEMENT);
        assertEquals(3, service.getResources().size());
    }
}
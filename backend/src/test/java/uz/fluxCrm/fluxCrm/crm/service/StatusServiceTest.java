package uz.fluxCrm.fluxCrm.crm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

public class StatusServiceTest {
    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private DefaultStatusService statusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDefault() {
        Pipeline pipeline = new Pipeline();
        pipeline.setId(1L);

        List<Status> expectedStatuses = new ArrayList<Status>();
        String[] defaultStatuses = new String[]{"Первичный контак", "Отправили договор", "Переговоры", "Принимают решение", "Успешно реализовано", "Закрыто и не реализовано"};
        for (String statusName : defaultStatuses) {
            Status status = new Status();
            status.setName(statusName);
            status.setPipeline(pipeline);
            expectedStatuses.add(status);
        }

        when(statusRepository.saveAll(anyList())).thenReturn(expectedStatuses);

        List<Status> createdStatuses = statusService.createDefault(pipeline);

        verify(statusRepository, times(1)).saveAll(anyList());

        assertEquals(6, createdStatuses.size(), "6 statuses should be created");

        createdStatuses.forEach(status -> assertEquals(pipeline, status.getPipeline()));

        assertEquals("Первичный контак", createdStatuses.get(0).getName());
        assertEquals("Отправили договор", createdStatuses.get(1).getName());
        assertEquals("Переговоры", createdStatuses.get(2).getName());
        assertEquals("Принимают решение", createdStatuses.get(3).getName());
        assertEquals("Успешно реализовано", createdStatuses.get(4).getName());
        assertEquals("Закрыто и не реализовано", createdStatuses.get(5).getName());
    }
}

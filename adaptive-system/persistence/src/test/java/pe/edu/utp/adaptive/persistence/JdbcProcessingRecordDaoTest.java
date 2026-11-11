package pe.edu.utp.adaptive.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JdbcProcessingRecordDaoTest {

    private ProcessingRecordDao dao;

    @BeforeEach
    void setUp() {
        Database db = new Database("jdbc:h2:mem:test" + System.nanoTime() + ";DB_CLOSE_DELAY=-1", "sa", "");
        dao = new JdbcProcessingRecordDao(db);
    }

    private ProcessingRecord sample(String strategy) {
        return new ProcessingRecord(null, strategy, "motivo de prueba", 120, 0.0, false, 3);
    }

    @Test
    void creaYLeeUnRegistro() {
        ProcessingRecord saved = dao.create(sample("fast"));
        assertTrue(dao.findById(saved.id()).isPresent());
        assertEquals("fast", dao.findById(saved.id()).get().strategyId());
    }

    @Test
    void listaTodosLosRegistros() {
        dao.create(sample("fast"));
        dao.create(sample("safe"));
        assertEquals(2, dao.findAll().size());
    }

    @Test
    void actualizaUnRegistro() {
        ProcessingRecord saved = dao.create(sample("fast"));
        ProcessingRecord changed = new ProcessingRecord(saved.id(), "precise", "cambio", 500, 0.1, true, 9);
        assertTrue(dao.update(changed));
        assertEquals("precise", dao.findById(saved.id()).get().strategyId());
    }

    @Test
    void borraUnRegistro() {
        ProcessingRecord saved = dao.create(sample("fast"));
        assertTrue(dao.delete(saved.id()));
        assertFalse(dao.findById(saved.id()).isPresent());
    }
}
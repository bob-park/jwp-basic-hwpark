package core.nmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ControllerScannerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ControllerScanner cf;

    @BeforeEach
    void setup(){
        cf = new ControllerScanner("core.nmvc");
    }

    @Test
    void getControllers() throws Exception{
        Map<Class<?>, Object> controllers = cf.getControllers();

        for(Class<?> controller: controllers.keySet()){
            logger.debug("controller : {}", controller);
        }
    }

}
package pl.nlp.day;

import org.junit.Test;
import pl.nlp.day.babelfy.BabelfyExtractor;

import java.util.Set;

import static java.lang.System.out;

public class ExperimentsBabelfyExtractor {

    @Test
    public void testBabelfyExtractor() {
        BabelfyExtractor babelfyExtractor = new BabelfyExtractor();

        babelfyExtractor.setResultSize(10);

        String text = "Europe's Schiaparelli Mars lander crashed last month after a sensor failure caused it to cast away its parachute and turn off braking thrusters more than two miles (3.7 km) above the surface of the planet, as if it had already landed, a report released on Wednesday said. The error stemmed from a momentary glitch in a device that measured how fast the spacecraft was spinning, the report by the European Space Agency said. \"When merged into the navigation system, the erroneous information generated an estimated altitude that was negative - that is, below ground level. This in turn successively triggered a premature release of the parachute ... and a brief firing of the braking thrusters,\" ESA said of its Oct. 19 attempt to land the Schiaparelli spacecraft on Mars. The spacecraft activated its ground systems, even though it was still about 2.3 miles off the surface, the ESA said. The crash of Schiaparelli onto the surface of Mars abruptly ended its mission to demonstrate a landing system and to measure wind speed and direction from the planet's surface. The prime contractor for the spacecraft is Italy's Thales Alenia Space, a joint venture of Thales SA (TCFP.PA) and Leonardo Finmeccanica SpA (LDOF.MI). A full report on the accident is expected in early 2017, ESA said ESA, which is based in Paris and has 22 member states, said the flight still provided information that will be key to landing a rover vehicle on Mars in 2021 that is designed to look for life past and present. \"We will have learned much from Schiaparelli,\" David Parker, who oversees space exploration programs for ESA, said in a statement.  (Reporting by Irene Klotz; Editing by Leslie Adler).";

        Set<String> result = babelfyExtractor.extract(text);

        for (String sc : result) {
            out.println(sc);
        }
    }

}

package kspcustomflightcontractgenerator;

import javax.swing.JFrame;

/**
 *
 * @author arthu
 */
public class KspCustomFlightCOntractGenerator {

    public static void main(String[] args) {

        // The generator outputs the specs to a file
        FlightGenerator generator = new FlightGenerator();

        // GUI for flight spec selection and file saving
        JFrame frame = new GUI(generator);

    }

}

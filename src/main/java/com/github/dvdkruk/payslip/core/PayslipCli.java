/**
 * Copyright (c) 2017, Damiaan van der Kruk.
 */
package com.github.dvdkruk.payslip.core;

import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;
import java.io.Console;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command line interface class for the monthly payslip application.
 *
 * @author Damiaan Van Der Kruk (Damiaan.van.der.Kruk@gmail.com)
 * @version $Id$
 * @since 1.0
 */
final class PayslipCli {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PayslipCli.class.getName());

    /**
     * Payslip processor.
     */
    private final PayslipProcessor processor = new PayslipProcessor();

    /**
     * System console.
     */
    private final Console console = System.console();

    /**
     * Main method for starting the CLI.
     *
     * @param args Program arguments.
     */
    public static void main(final String... args) {
        new PayslipCli().start(args);
    }

    /**
     * Starts the program with the given args.
     *
     * @param args Program arguments.
     */
    private void start(final String... args) {
        if (this.console == null) {
            throw new PayslipCliRuntimeException();
        }
        if (args.length == 0) {
            this.runInteractiveMode();
        } else {
            this.runOnce(args);
        }
    }

    /**
     * Executed the interactive mode.
     */
    private void runInteractiveMode() {
        this.printInteractiveModeText();
        boolean running = true;
        while (running) {
            running = this.parseLine();
        }
    }

    /**
     * Parses a line from the {@link PayslipCli#console}.
     *
     * @return Is {@code false} when exit is parsed, else {@code true} is
     *  returned.
     */
    private boolean parseLine() {
        final String line = this.console.readLine();
        boolean proceed = true;
        if (!isNullOrEmpty(line)) {
            if ("exit".equals(line)) {
                proceed = false;
            } else {
                this.parse(line);
            }
        }
        return proceed;
    }

    /**
     * Prints the interactive mode helper text.
     */
    private void printInteractiveModeText() {
        final PrintWriter writer = this.console.writer();
        writer.println("Employee Monthly Payslip Tool - Interactive Mode");
        final String format =
            "<first_name>,<last_name>,<annual_salary>,<super_rate>%,<month>";
        writer.println(String.format("Request format: %s", format));
        final String example = "David,Rudd,60050,9%,March";
        writer.println(String.format("For example: %s%n", example));
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param string String that needs to be checked.
     * @return True when string is null or empty, else it returns false.
     */
    private static boolean isNullOrEmpty(final String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Parses and executes the given program argument.
     *
     * @param line A String containing the program arguments.
     */
    private void parse(final String line) {
        this.parse(line, -1);
    }

    /**
     * Run once with the given arguments.
     *
     * @param args The arguments for running the program.
     */
    private void runOnce(final String... args) {
        for (int index = 0; index < args.length; ++index) {
            this.parse(args[index], index);
        }
    }

    /**
     * Parses and executes the given line. The index is used for given hints
     * when to what argument is couldn't be successfully executed. When only one
     * argument is, used index -1 or lower to disable argument number hints.
     *
     * @param line A String containing the program arguments.
     * @param index Index for the argument number hint.
     */
    private void parse(final String line, final int index) {
        try {
            final PayslipRequestParser parser = new PayslipRequestParser(line);
            final PayslipRequest request = parser.toPayslipRequest();
            final PayslipResult result = this.processor.process(request);
            this.console.writer().println(result.toString());
        } catch (final PayslipException pex) {
            this.handle(pex, index);
        }
    }

    /**
     * Handles {@code pex} for argument {@code index}.
     *
     * @param pex Exception that need to be handled.
     * @param index Argument index.
     */
    private void handle(final PayslipException pex, final int index) {
        if (index < 0) {
            final String msg =
                String.format("error, argument: %s", index);
            this.console.writer().println(msg);
            LOG.log(Level.FINE, msg, pex);
        } else {
            this.console.writer().println(pex.getMessage());
            LOG.log(Level.FINE, "error", pex);
        }
    }

    /**
     * PayslipCliRuntimeException.
     */
    static class PayslipCliRuntimeException extends RuntimeException {

        /**
         * Constructor for PayslipCliRuntimeException.
         */
        PayslipCliRuntimeException() {
            super("no console");
        }
    }

}

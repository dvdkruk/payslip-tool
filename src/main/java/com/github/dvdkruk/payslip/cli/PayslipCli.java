package com.github.dvdkruk.payslip.cli;

import com.github.dvdkruk.payslip.PayslipProcessor;
import com.github.dvdkruk.payslip.model.PayslipException;
import com.github.dvdkruk.payslip.model.PayslipRequest;
import com.github.dvdkruk.payslip.model.PayslipResult;

import java.io.Console;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command line interface class for the monthly payslip application.
 */
class PayslipCli {

    private static final Logger LOG = Logger.getLogger(PayslipCli.class.getName());

    private final PayslipProcessor processor = new PayslipProcessor();

    private final Console console = System.console();

    private PayslipCli() {
        if (console == null) {
            throw new PayslipCliRuntimeException("no console");
        }
    }

    public static void main(String[] args) {
        PayslipCli cli = new PayslipCli();
        cli.start(args);
    }

    private void start(String[] args) {
        if (args.length != 0) {
            runOnce(args);
        } else {
            runInteractiveMode();
        }
    }

    private void runInteractiveMode() {
        console.writer().println("Employee Monthly Payslip Tool - Interactive Mode");
        console.writer().println("Request format: <first_name>,<last_name>,<annual_salary>,<super_rate>%,<month>");
        console.writer().println("For example: David,Rudd,60050,9%,March\n");

        boolean running = true;
        do {
            String line = console.readLine();
            if (line != null && !line.isEmpty()) {
                if ("exit".equals(line)) {
                    running = false;
                } else {
                    parse(line);
                }
            }
        } while (running);
    }

    private void parse(String line) {
        try {
            PayslipRequest request = PayslipRequest.parse(line);
            PayslipResult result = processor.process(request);
            console.writer().println(result.toString());
        } catch (PayslipException e) {
            console.writer().println(e.getMessage());
            LOG.log(Level.FINE, e.getMessage(), e);
        }
    }

    private void runOnce(String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                PayslipRequest request = PayslipRequest.parse(args[i]);
                PayslipResult result = processor.process(request);
                console.writer().println(result.toString());
            } catch (PayslipException e) {
                console.writer().println("(argument: " + i + "): " + e.getMessage());
                LOG.log(Level.FINE, "(argument: " + i + "): " + e.getMessage(), e);
            }
        }
    }

    static class PayslipCliRuntimeException extends RuntimeException {

        PayslipCliRuntimeException(String s) {
            super(s);
        }
    }

}

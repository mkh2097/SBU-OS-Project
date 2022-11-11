import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Hospital {

    private static Hospital hospital;

    private final int number_of_doctors;
    private final int number_of_chairs;

    private Integer number_of_available_doctors;
    private final Queue<Patient> patientList;

    private final Semaphore number_mutex;
    private final Semaphore queue_mutex;
    private final Semaphore patient_mutex;
    private final Semaphore enter_mutex;
    private final BoundedSemaphore doctor_mutex;


    private Hospital(int number_of_doctors, int number_of_chairs) {
        this.number_of_chairs = number_of_chairs;
        this.number_of_doctors = number_of_doctors;
        this.number_of_available_doctors = number_of_doctors;
        this.number_mutex = new Semaphore(1);
        this.queue_mutex = new Semaphore(1);
        this.enter_mutex = new Semaphore(1);
        this.doctor_mutex = new BoundedSemaphore(1);


        this.patient_mutex = new Semaphore(0);
        patientList = new LinkedList<>();
    }

    public static Hospital getInstance(int number_of_doctors, int number_of_chairs) {
        if (hospital == null) {
            hospital = new Hospital(number_of_doctors, number_of_chairs);
        }
        return hospital;
    }

    public int getNumber_of_chairs() {
        return number_of_chairs;
    }

    public int getNumber_of_doctors() {
        return number_of_doctors;
    }

    public void checkForPatient(Doctor doctor) throws InterruptedException {

        patient_mutex.acquire();



        queue_mutex.acquire();
        Patient patient = patientList.poll();
        queue_mutex.release();


        doctor_mutex.release();



        number_mutex.acquire();
        number_of_available_doctors--;
        number_mutex.release();

        Thread.sleep(2000);

        System.out.println("Visit Finished by Doctor No." + doctor.getId() + " & Patient No." + patient.getName() + " in time " + (new Date().getTime() - Monitor.getStartOfProgram()) / 1000.0);

        number_mutex.acquire();
        number_of_available_doctors++;
        number_mutex.release();


    }

    public void goToHospital(Patient patient) throws InterruptedException {
        enter_mutex.acquire();
        queue_mutex.acquire();



        if (patientList.size() == number_of_chairs) {
            System.out.println("Not Available Doctor --> Patient no." + patient.getName() + " exited at: " + (new Date().getTime() - Monitor.getStartOfProgram()) / 1000.0);
            queue_mutex.release();

        } else if (number_of_available_doctors > 0) {

            patientList.offer(patient);
            patient_mutex.release();
            queue_mutex.release();
            doctor_mutex.acquire();

        } else { // Took a Seat or Available Doctor
            patientList.offer(patient);
            patient_mutex.release();
            queue_mutex.release();
        }
        enter_mutex.release();
    }
}


class BoundedSemaphore {
    int signal;
    int capacity;

    public BoundedSemaphore(int cap) {
        signal = 0;
        capacity = cap;
    }

    public synchronized void release() throws InterruptedException {
        while(signal == capacity) {
            wait();
        }
        if(signal == 0) {
            notifyAll();
        }
        signal ++;
    }

    public synchronized void acquire() throws InterruptedException {
        while(signal == 0) {
            wait();
        }
        if(signal == capacity) {
            notifyAll();
        }
        signal --;
    }
}
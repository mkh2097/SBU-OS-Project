import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Monitor {

    private static Hospital hospital;
    private static long startOfProgram;

    private static List<Patient> patients;

    public static long getStartOfProgram() {
        return startOfProgram;
    }

    public static Hospital getHospital() {
        return hospital;
    }

    public static List<Patient> getPatients() {
        return patients;
    }

    public static void main(String[] args) throws InterruptedException {


        int number_of_doctors;
        int number_of_chairs;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter n:");
        number_of_doctors = scanner.nextInt();
        System.out.println("Enter m");
        number_of_chairs = scanner.nextInt();

        hospital = Hospital.getInstance(number_of_doctors, number_of_chairs);
        System.out.println("A New hospital initiated with "+ hospital.getNumber_of_doctors() + " doctors & "+hospital.getNumber_of_chairs()+" chairs \n");


        ExecutorService exec = Executors.newFixedThreadPool(12);


        for (int i = 0; i < number_of_doctors ; i++){
            Thread doctor_thread = new Thread(new Doctor(i+1));
            exec.execute(doctor_thread);
        }


//         List<Patient> patients = new ArrayList<>(Arrays.asList(
//                 new Patient("user1", 3),
//                 new Patient("user2", 1),
//                 new Patient("user3", 1),
//                 new Patient("user4", 4),
//                 new Patient("user5", 3),
//                 new Patient("user6", 1),
//                 new Patient("user7", 1),
//                 new Patient("user8", 4),
//                 new Patient("user9", 3),
//                 new Patient("user10", 1),
//                 new Patient("user11", 1),
//                 new Patient("user12", 4)));


        patients = new ArrayList<>(Arrays.asList(
                new Patient("a", 0),
                new Patient("d", 0),
                new Patient("o", 13),
                new Patient("s", 14),
                new Patient("r", 13),
                new Patient("p", 13),
                new Patient("k", 12),
                new Patient("i", 2),
                new Patient("j", 3),
                new Patient("c", 0),
                new Patient("b", 0),
                new Patient("f", 0),
                new Patient("e", 0),
                new Patient("g", 0),
                new Patient("h", 0),
                new Patient("l", 12),
                new Patient("q", 13),
                new Patient("m", 12),
                new Patient("n", 13)));

        patients = new ArrayList<>(Arrays.asList(
                new Patient("user10", 1),
                new Patient("user11", 1),
                new Patient("user12", 1),
                new Patient("user13", 1),
                new Patient("user14", 1),
                new Patient("user15", 1),
                new Patient("user16", 1),
                new Patient("user17", 3),
                new Patient("user18", 3),
                new Patient("user19", 3),
                new Patient("user20", 3),
                new Patient("user21", 3),
                new Patient("user22", 3),
                new Patient("user23", 3),
                new Patient("user24", 5),
                new Patient("user25", 5),
                new Patient("user26", 9),
                new Patient("user27", 10),
                new Patient("user28", 11),
                new Patient("user29", 12),
                new Patient("user30", 13)));



        patients.sort(Comparator.comparingInt(Patient::getTime));

        ArrayList<Integer> delays = new ArrayList<>();

        int first_time = patients.get(0).getTime();
        int first_time_save = first_time;

        for (Patient patient : patients){
            delays.add(patient.getTime() - first_time);
            first_time = patient.getTime();
        }

        delays.set(0, first_time_save);
        startOfProgram = new Date().getTime();
        for (int i = 0; i < patients.size(); i++){
            try {
                Thread.sleep(delays.get(i) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread patient_thread = new Thread(patients.get(i));
            exec.execute(patient_thread);
        }
        exec.shutdown();
    }
}

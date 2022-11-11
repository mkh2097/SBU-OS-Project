public class Doctor implements Runnable{

    private final int id;

    public Doctor(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
//        System.out.println("new Doctor Created with id : " + this.id + "\n");
        while (true){
            try {
                Monitor.getHospital().checkForPatient(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

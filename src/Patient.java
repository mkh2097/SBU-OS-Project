public class Patient implements Runnable{

    private final String name;
    private final int time;

    public Patient(String name, int time){
        this.name = name;
        this.time = time;
    }



    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }


    @Override
    public void run() {
//        System.out.println("new Patient Created with id : " + this.name +" At time:"+ this.time + "\n");
        try {
            Monitor.getHospital().goToHospital(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

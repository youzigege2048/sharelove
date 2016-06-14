package youzi.com.sharelove.modal;

/**
 * Created by youzi on 2016/6/12.
 */
public class TThread extends Thread {
    String control = "";

    public void suspendS() {
        synchronized (control) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeS() {
        this.notify();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (control) {
                try {
                    Thread.sleep(2000);
                    System.out.println(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

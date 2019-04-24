package Validation;

public class SalesforceHandlerThread extends Thread {

    private String un;
    private String pw;

    private static long startTime;

    public SalesforceHandlerThread(String username, String password, String threadName) {
        super(threadName);
        this.un = username;
        this.pw = password;

        startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        super.run();

        SalesforceHepler hepler = new SalesforceHepler(un, pw);
        hepler.processUser();
    }

    @Override
    public synchronized void start() {
        super.start();
        System.out.println(Thread.currentThread().getName() + ": >> Started thread: " + super.getName());
    }

    public static long getStartTime() {
        return startTime;
    }
}

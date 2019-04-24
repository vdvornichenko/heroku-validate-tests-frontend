package Validation;

public class ValidationTaskMain {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start main thread.");

        GoogleHelper.callDocument();

        SalesforceHepler.checkUsersResults();
    }
}

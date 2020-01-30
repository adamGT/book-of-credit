package inc.bado.app.models;

public class General {
    private String title;
    private String name;
    private String amount;
    private boolean isCredit;

    public General(String title, String name, String amount,boolean isCredit) {
        this.title = title;
        this.name = name;
        this.amount = amount;
        this.isCredit = isCredit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }
}

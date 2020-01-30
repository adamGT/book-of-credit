package inc.bado.app.models;

public class Credit {
    private String title;
    private String name;
    private String amount;

    public Credit(String title, String name, String amount) {
        this.title = title;
        this.name = name;
        this.amount = amount;
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
}

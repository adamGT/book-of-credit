package inc.bado.app.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import inc.bado.app.utils.DateTypeConverter;

@Entity(tableName = "general")
public class General {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "general_title")
    private String title;
    @ColumnInfo(name = "general_name")
    private String name;
    @ColumnInfo(name = "general_amount")
    private float amount;
    @TypeConverters(DateTypeConverter.class)
    @ColumnInfo(name = "general_time")
    @NonNull
    private Date time;

    @ColumnInfo(name = "general_iscredit")
    private boolean isCredit;

    public General(String title, String name, float amount, Date time, boolean isCredit) {
        this.title = title;
        this.name = name;
        this.amount = amount;
        this.time = time;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    @NonNull
    public Date getTime() {
        return time;
    }

    public void setTime(@NonNull Date time) {
        this.time = time;
    }

}

package inc.bado.app.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import inc.bado.app.utils.DateTypeConverter;

@Entity(tableName = "debit")
public class Debit {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "debit_title")
    private String title;
    @ColumnInfo(name = "debit_name")
    private String name;
    @ColumnInfo(name = "debit_amount")
    private float amount;
    @ColumnInfo(name = "debit_time")
    @TypeConverters(DateTypeConverter.class)
    @NonNull
    private Date time;

    public Debit(String title, String name, float amount,Date time) {
        this.title = title;
        this.name = name;
        this.amount = amount;
        this.time = time;
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

    @NonNull
    public Date getTime() {
        return time;
    }

    public void setTime(@NonNull Date time) {
        this.time = time;
    }
}

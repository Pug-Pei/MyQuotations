package co.websarva.wings.android.myquotations.quotations;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotations")
public class Quotations implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "authorName")
    private String authorName;

    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "checkBox")
    private String checkBox;

    public Quotations(String title, String authorName, String timeStamp, String content, String checkBox) {

        this.title = title;
        this.authorName = authorName;
        this.timeStamp = timeStamp;
        this.content = content;
        this.checkBox = checkBox;
    }

    @Ignore
    public Quotations() {
    }


   protected Quotations(Parcel in) {
        id = in.readInt();
        title = in.readString();
        authorName = in.readString();
        timeStamp = in.readString();
        content = in.readString();
        checkBox = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(authorName);
        dest.writeString(timeStamp);
        dest.writeString(content);
        dest.writeString(checkBox);
    }

    public static final Creator<Quotations> CREATOR = new Creator<Quotations>() {
        @Override
        public Quotations createFromParcel(Parcel in) {
            return new Quotations(in);
        }

        @Override
        public Quotations[] newArray(int size) {
            return new Quotations[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(String checkBox) {
        this.checkBox = checkBox;
    }



    @Override
    public String toString() {
        return "Quotations{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", content='" + content + '\'' +
                ", checkBox='" + checkBox + '\'' +
                '}';
    }
}

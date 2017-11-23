package ai.api.sample;

/**
 * Class that represent the message.
 */
public class Chat {
    private String username;
    private String message;
    private String number;
    private String urun;
    private String sekil;
    private String type;
    private int id;
    private String time;
    private String lat;
    private String lng;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Chat() {
    }

    public Chat(String username , String message, int id, String time) {
        this.username = username;
        this.message = message;
        this.id = id;
        this.time = time;
    }
    public Chat(String username , String number, String urun, String sekil, String type, int id, String time) {
        this.username = username;
        this.number = number;
        this.urun = urun;
        this.sekil = sekil;
        this.type = type;
        this.id = id;
        this.time = time;
    }

    public Chat(String username, String message, String lat, String lng, int id, String time) {
        this.username = username;
        this.message = message;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSekil() {
        return sekil;
    }

    public void setSekil(String sekil) {
        this.sekil = sekil;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrun() {
        return urun;
    }

    public void setUrun(String urun) {
        this.urun = urun;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}

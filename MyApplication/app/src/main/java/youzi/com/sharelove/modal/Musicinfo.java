package youzi.com.sharelove.modal;

/**
 * Created by youzi on 2016/6/10.
 */
public class Musicinfo {
    public String id;
    public String name;//歌名
    public String author;//作者
    public String dir;//歌曲目录
    public String lrc;//歌词目录
    public String rate;
    public String num;
    public String signature;

    public Musicinfo(String id, String name, String author, String dir, String lrc, String rate, String num, String signature) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.dir = dir;
        this.lrc = lrc;
        this.rate = rate;
        this.num = num;
        this.signature = signature;
    }

    public Musicinfo(String id, String name, String author, String dir, String lrc, String rate) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.dir = dir;
        this.lrc = lrc;
        this.rate = rate;
    }

    public Musicinfo(String id, String name, String author, String dir, String lrc) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.dir = dir;
        this.lrc = lrc;
    }

    public Musicinfo(String name, String author, String dir, String lrc) {
        this.name = name;
        this.author = author;
        this.dir = dir;
        this.lrc = lrc;
    }
}

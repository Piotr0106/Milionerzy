package pl.gda.wsb.milionerzy;

public class Question {
    private String quest, a,b,c,d,answer;

    public Question(String quest, String a, String b, String c, String d, String answer){
        this.quest = quest;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
    }

    public String getQuest() { return quest; }
    public String getA() { return a; }
    public String getB() { return b; }
    public String getC() { return c; }
    public String getD() { return d; }
    public String getAnswer() { return answer; }
}

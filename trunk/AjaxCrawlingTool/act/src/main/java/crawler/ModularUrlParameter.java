package crawler;

public class ModularUrlParameter {

    String param;
    String value;
    
    public ModularUrlParameter(String parameter){
            String[] split = parameter.split("=");
            param = split[0];
            value = split[1];
    }

    public String getParameter() {
            return param;
    }

    public void setParameter(String param) {
            this.param = param;
    }

    public String getValue() {
            return value;
    }

    public void setValue(String value) {
            this.value = value;
    }
    
    public String getCompleteParameter(){
            return param + "=" + value;
    }
}
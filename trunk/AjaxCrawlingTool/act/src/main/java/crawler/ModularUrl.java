package crawler;

import java.util.ArrayList;

public class ModularUrl {

        String base;
        ArrayList<ModularUrlParameter> params;
        
        public ModularUrl(String url){
                params = null;
                String[] baseSplit = url.split("[?]");
                base = baseSplit[0];
                //split parameters up
                if(baseSplit.length > 1){
                        String[] paramSplit = baseSplit[1].split("&");
                        params = new ArrayList<ModularUrlParameter>();
                        for(int i = 0; i < paramSplit.length; i++){
                                params.add(new ModularUrlParameter(paramSplit[i]));
                        }
                }
        }
        
        
        public String getAllParametersAsString(){
                StringBuilder result = new StringBuilder();
                for(int i = 0; i<params.size();i++){
                        result.append(params.get(i).getCompleteParameter());
                        if(i != params.size()-1){
                                result.append("&");
                        }
                }
                return new String(result);
        }
        
        public String getCompleteUrl(){
                return base + "?" + this.getAllParametersAsString();
        }


        public String getBase() {
                return base;
        }


        public void setBase(String base) {
                this.base = base;
        }


        public ArrayList<ModularUrlParameter> getParams() {
                return params;
        }


        public void setParams(ArrayList<ModularUrlParameter> params) {
                this.params = params;
        }

}
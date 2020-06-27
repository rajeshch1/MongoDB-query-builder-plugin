import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import com.mongodb.*;
import java.util.regex.Pattern;


@Named(value = "mongo")
@SessionScoped
public class Mongo implements Serializable {

        private String output,fld1,fld2,oper,val;
        private String temp[];
        private int i,flag;
        private DBObject query;
        private QueryBuilder obj1;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getFld1() {
        return fld1;
    }

    public void setFld1(String fld1) {
        this.fld1 = fld1;
    }

    public String getFld2() {
        return fld2;
    }

    public void setFld2(String fld2) {
        this.fld2 = fld2;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }
      
        public Mongo() {
            val = "#";
            
    }

    public void Process()
    {
        if(val.equals("#"))
        {
            val = "start()~put("+getFld1()+")~"+getOper()+"("+getFld2()+")";
        }
        else
        {
            val = val + "~and("+getFld1()+")~"+getOper()+"("+getFld2()+")";
        }
        temp = val.split("~");
        flag=0;
        for(int i = 0; i < temp.length; i++)
        {   
            if(i==0)
                obj1 = new QueryBuilder();
            else 
            {
                if(flag==0)
                {
                    flag = 1;
                    if(temp[i].substring(0,3).equals("put"))
                    obj1.put(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                    else
                    obj1.and(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));    
                    
                }
                else
                {
                flag = 0;
                if(temp[i].substring(0,2).equals("is"))
                    obj1.is(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,3).equals("gte"))
                    obj1.greaterThanEquals(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,2).equals("gt"))
                   obj1.greaterThan(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,3).equals("lte"))
                    obj1.lessThanEquals(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,2).equals("lt"))
                   obj1.lessThan(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,2).equals("ne"))
                   obj1.notEquals(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")")));
                else if(temp[i].substring(0,5).equals("regex"))
                   obj1.regex(Pattern.compile(temp[i].substring(temp[i].lastIndexOf("(")+1,temp[i].lastIndexOf(")"))));
                }
            }
        }
            query = (DBObject) obj1.get();
            output = query.toString();
            setOutput(output);
    }
    
    public void Resetnow()
    {
        setFld1("");
        setFld2("");
        setOutput("");
        val = "#";
    }
    
}

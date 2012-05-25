import java.util.HashMap;
import net.sf.json.*;
class Message
{
    private JSONObject args;

    public Message() 
    {
	args = new JSONObject();
    }

    public Message(String message)
    {
	
	args = (JSONObject) JSONSerializer.toJSON(message); 
    } 

    public void setArg(String argName, Object arg)
    {
	args.put(argName,arg);
    }

    public Object getArg(String argName)
    {
	if(args.has(argName))
	    return args.get(argName);
	return null;
    }

    public Boolean has(String argName)
    {
	if(getArg(argName) == null)
	    return false;
	return true;
    }

    public String getMessage()
    {
	return args.toString();
    }   

}
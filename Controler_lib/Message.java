class Message
{
    
    public enum Type {ADD, TEXT, MUSIC, GOSTRAIGHT, TURNLEFT, TURNRIGHT, ACK, ERROR, UNKNOWN};

    private String to;
    private String from;
    private String body;
    private Type type;
    
    static private String SEPARATOR = ";";

    public void buildMessage(String _from, String _to, Type _type, String _body)
    {
	to = _to;
	from = _from;
	type = _type;
	body = _body;
    }

    public Message() {}

    public Message(String _from, String _to, Type _type, String _body)
    {
	buildMessage(_from,_to,_type,_body);
    }

    public Message(String message_s)
    {
	String [] tmp = message_s.split(SEPARATOR);
	buildMessage(tmp[0],tmp[1],stringToType(tmp[2]),tmp[3]);
    }

    public String getMessage()
    {
	return getFrom()+SEPARATOR
	    +getTo()+SEPARATOR
	    +getType()+SEPARATOR
	    +getBody();
    }

    public String getTo() {return to;}

    public String getFrom() {return from;}

    public String getBody() {return body;}

    public Type getType() {return type;}

    public static String typeToString(Type _type)
    {
	switch(_type)
	    {
	    case ADD: return "ADD";
	    case TEXT: return "TEXT";
	    case MUSIC: return "MUSIC";
	    case GOSTRAIGHT: return "GOSTRAIGHT";
	    case TURNLEFT: return "TURNLEFT";
	    case TURNRIGHT: return "TURNRIGHT";
	    case ACK: return "ACK";
	    case ERROR: return "ERROR";
	    default: return "UNKNOWN";
	    }
    }

    public static Type stringToType(String _type)
    {
	if(_type.equals("ADD")) return Type.ADD;
	if(_type.equals("TEXT")) return Type.TEXT;
	if(_type.equals("MUSIC")) return Type.MUSIC;
	if(_type.equals("GOSTRAIGHT")) return Type.GOSTRAIGHT;
	if(_type.equals("TURNLEFT")) return Type.TURNLEFT;
	if(_type.equals("TURNRIGHT")) return Type.TURNRIGHT;
	if(_type.equals("ACK")) return Type.ACK;
	if(_type.equals("ERROR")) return Type.ERROR;
	return Type.UNKNOWN;	    
    }
    

    public Message reply(String _body)
    {
	return new Message(to,from,type,_body);
    }

    public Message reply_success(String _body)
    {
	return new Message(to,from,Type.ACK,_body);
    }

    public Message reply_error(String _body)
    {
	return new Message(to,from,Type.ERROR,_body);
    }

}
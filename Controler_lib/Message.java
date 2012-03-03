class Message
{
    public enum Type {TEXT, MUSIC, GOSTRAIGHT, TURNLEFT, TURNRIGHT};

    private String to;
    private String from;
    private String body;
    private Type type;
    

    public Message(String message_s)
    {
	
	to = "";
	from = "";
	body = "";
    }

    public Message(String _from, String _to, Type _type, String _body)
    {
	to = _to;
	from = _from;
	type = _type;
	body = _body;
    }
    
    public String getTo() {return to;}
    public String getFrom() {return from;}
    public String getBody() {return body;}
    public String getType() 
    {
	switch(type)
	    {
	    case TEXT: return "TEXTE";
	    case MUSIC: return "MUSIC";
	    case GOSTRAIGHT: return "GOSTRAIGHT";
	    case TURNLEFT: return "TURNLEFT";
	    case TURNRIGHT: return "TURNRIGHT";
	    default: return "UNKNOWN";
	    }
    }
}
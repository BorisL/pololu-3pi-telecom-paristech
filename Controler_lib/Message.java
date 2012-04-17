class Message
{
    
    public enum Type {ADD, TEXT, MUSIC, GOSTRAIGHT, TURNLEFT, TURNRIGHT, ACK, ERROR, UNKNOWN};
    public enum State {SUBMIT, SUCCESS, ERROR, UNKNOWN};

    private Integer id;
    private String to;
    private String from;
    private Type type;
    private String body;
    private State state;
    private Integer errno;
    
    static private String SEPARATOR = ";";

    public void buildMessage(Integer _id, String _from, String _to, Type _type, String _body, State _state, Integer _errno)
    {
	id = _id;
	to = _to;
	from = _from;
	type = _type;
	body = _body;
	state = _state;
	errno = _errno;
    }

    public Message() {}

    public Message(String _from, String _to, Type _type, String _body)
    {
	buildMessage(-1, _from,_to,_type,_body, State.SUBMIT, 0);
    }

    public Message(String message_s)
    {
	String [] tmp = message_s.split(SEPARATOR);
	buildMessage(
		     new Integer(tmp[0]), // id
		     tmp[1], // from
		     tmp[2], // to
		     stringToType(tmp[3]), // type
		     tmp[4], // body
		     stringToState(tmp[5]), // state
		     new Integer(tmp[6])  // errno
		     );
    }

    public String getMessage()
    {
	return getId()+SEPARATOR
	    +getFrom()+SEPARATOR
	    +getTo()+SEPARATOR
	    +getType()+SEPARATOR
	    +getBody()+SEPARATOR
	    +getState()+SEPARATOR
	    +getErrno();
    }

    public String getTo() {return to;}

    public String getFrom() {return from;}

    public String getBody() {return body;}

    public Type getType() {return type;}

    public State getState() {return state;}

    public Integer getErrno() {return errno;}

    public Integer getId() {return id;}

    public void setState(State _state) {state = _state;}

    public void setErrno(Integer _errno) {errno = _errno;}

    public void setId(Integer _id) {id = _id;}

    public void setFrom(String _from) {from = _from;}

    public void setTo(String _to) {to = _to;}

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

    public static String stateToString(State _state)
    {
	switch(_state)
	    {
	    case SUBMIT: return "SUBMI";
	    case SUCCESS: return "SUCCESS";
	    case ERROR: return "ERROR"; 
	    default: return "UNKNOWN";
	    }
    }

    public static State stringToState(String _state)
    {
	if(_state.equals("SUBMIT")) return State.SUBMIT;
	if(_state.equals("SUCCESS")) return State.SUCCESS;
	if(_state.equals("ERROR")) return State.ERROR;
	return State.UNKNOWN;	
    }

    public void reply_success()
    {
	String to_tmp = to;
	to = from;
	from = to_tmp;
	setState(State.SUCCESS);
    }

    public void reply_error(Integer _errno)
    {
	String to_tmp = to;
	to = from;
	from = to_tmp;
	setState(State.ERROR);
	setErrno(_errno);
    }

}
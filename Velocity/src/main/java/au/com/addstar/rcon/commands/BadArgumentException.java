package au.com.addstar.rcon.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BadArgumentException extends RuntimeException
{
	private static final long serialVersionUID = -5099437186563532852L;
	
	private int mArg;
	private ArrayList<String> mInfoLines;
	
	public BadArgumentException(int argument)
	{
		mArg = argument;
	}
	
	public BadArgumentException(int argument, String reason)
	{
		super(reason);
		mArg = argument;
	}
	
	public int getArgument()
	{
		return mArg;
	}
	public void setArgument(int argument)
	{
		mArg = argument;
	}
	
	public BadArgumentException addInfo(String line)
	{
		if(mInfoLines == null)
			mInfoLines = new ArrayList<>();
		mInfoLines.add(line);
		
		return this;
	}
	public BadArgumentException addInfo(Collection<String> lines)
	{
		if(mInfoLines == null)
			mInfoLines = new ArrayList<>();
		mInfoLines.addAll(lines);
		
		return this;
	}
	
	public List<String> getInfoLines()
	{
		if(mInfoLines == null)
			return Collections.emptyList();
		return mInfoLines;
	}
}

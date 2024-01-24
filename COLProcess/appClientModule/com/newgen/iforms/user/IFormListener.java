package com.newgen.iforms.user;


import com.newgen.iforms.custom.IFormListenerFactory;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.iforms.custom.IFormServerEventHandler;

public class IFormListener implements IFormListenerFactory{

	@Override
	public IFormServerEventHandler getClassInstance(IFormReference iformobj) {
		// TODO Auto-generated method stub
				System.out.println("--------------------------COL------------------");
				String procName =iformobj.getProcessName();
				System.out.println("procName:"+procName);
				if("COL".equalsIgnoreCase(procName))
				{
					return  new RLOS_Call();
				}
				
				return null;
	}
	}


import java.util.*;

public class RoutingMapTree2{
	Exchange root;
	RoutingMapTree(){
		root = new Exchange(0);
		root.parent = null;
		root.residentSet = new MobilePhoneSet();
		root.children = new ExchangeList();
	}
	RoutingMapTree(Exchange a){
		this.root = a;
		//System.out.println(root.identity);
		this.root.identity = a.identity;
	}
	public Boolean containsNode(Exchange a){
		if(root == a)return true;
		else{
			for(int i=0;i<root.numChildren();i++){
				root.subtree(i).containsNode(a);
			}
		}
		return false;
	}	
	public Boolean containsNode(int id){
		if(this.root.identity == id)return true;
	
			for(int i=0;i<root.numChildren();i++){
				if(root.subtree(i).containsNode(id))
					return true;
			}
	
		return false;
	}
	public Exchange getNode(int id){
		
		
		if(this.root.identity == id) {
		//	System.out.println(root.identity);
		
			return this.root;
		}

		
		for(int i=0;i<root.numChildren();i++){
			if(this.root.subtree(i).containsNode(id))
				return (this.root.subtree(i).getNode(id));
			
		}
		return root;
	}
	public void switchOn(MobilePhone a, Exchange b){
		//if(this.root.residentSet.Phones.IsMember(a)){
		if(!a.status()){
			a.switchOn();
			try{
				if(a.location()!=null){
					Exchange c = a.location();
					//System.out.println("You are At node "+c.identity);
						while(!c.isRoot()){

					//		System.out.println("HELL O "+c.identity);
							c.residentSet.Phones.Delete(a);
								c = c.parent();
						}
					c.residentSet.Phones.Delete(a);
				}
			}
			catch(Exception e){
				System.out.println("Mobile Phone not found");
			}
			finally{
				a.setExchange(b);
				Exchange temp = b;
				while(!temp.isRoot()){
					temp.residentSet.Phones.Insert(a);
					temp = temp.parent();
				}
				temp.residentSet.Phones.Insert(a);
			}
		}
	}
	public void switchOff(MobilePhone a){
		
		/*	Exchange b = a.location();
			while(!b.isRoot()){
				b.residentSet.Phones.Delete(a);
					b = b.parent();
			}
			b.residentSet.Phones.Delete(a);	
		*/	
			a.switchOff();
			//System.out.println("Now "+a.number()+" is "+a.status());
	}
	public Exchange findPhone(MobilePhone m){
		try{
			return m.location();
		}
		catch(NullPointerException en){
			return null;
		}
	}
	public Exchange lowestRouter(Exchange a, Exchange b){
		
		Exchange temp1 = a, temp2 =b;
		while(temp1!=temp2 && !temp1.isRoot()){
			temp2 = b;
			while(temp1 != temp2 && !temp2.isRoot()){
				temp2 = temp2.parent();
			}
			if(temp1 == temp2)
				break;
			temp1 = temp1.parent();
		}
		return temp1;
	}
	public ExchangeList routeCall(MobilePhone a, MobilePhone b){
		Exchange e1=a.location();
		Exchange e2=b.location();
		Exchange c = lowestRouter(e1,e2);
		ExchangeList route = new ExchangeList(e1);
		if(e1!=c){
			Exchange temp = e1.parent(); 
			while(temp != c){
				route.list.Insert(temp);
				temp = temp.parent();
			}
			route.list.Insert(temp);
			ExchangeList route1 = new ExchangeList(e2);
			Exchange temp2 = e2.parent(); 
			while(temp2.parent != c){
				route1.list.Insert(temp2);
				temp2 = temp2.parent();
			}
			route1.list.Insert(temp2);
			linkedlist.node node = route1.list.head;
			while(node.next!=null){
				while(node.next.next != null){
					node = node.next.next;
				}
				route.list.Insert(node.next.data);
				node.next = null;
			}
			route.list.Insert(node.data);
		}

		return route;
	}
	public void movePhone(MobilePhone a, Exchange b){
		if(a.status()){
			a.switchOff();
			switchOn(a,b);
		}
	}
	public String performAction(String actionMessage){
		int a,b;
		Exchange node,base;
		Scanner s = new Scanner(actionMessage);

		switch(s.next()){
			case "addExchange":
				a = s.nextInt();
				b = s.nextInt();
				if(this.containsNode(a)){
					Exchange parentnode = this.getNode(a);
					if(!this.containsNode(b)){
						node = new Exchange(b);
						node.parent = 	parentnode;
						parentnode.children.list.Insert(node);
						node.residentSet = new MobilePhoneSet();
						node.children = new ExchangeList(); 
						//actionMessage = "";
					}
					else{
						actionMessage = actionMessage+ ": Error- exchange with identifier "+b+" already exists";
					}				
				}
				else{
					actionMessage = actionMessage+ ": Error- No exchange with identifier "+a;
				}

				break;
			case "switchOnMobile":
				a = s.nextInt();
				b = s.nextInt();
				if(this.containsNode(b)){
					node = this.getNode(b);
					MobilePhone c = this.root.residentSet.FindPhone(a);
					if(c==null){
						MobilePhone add = new MobilePhone(a);
						this.switchOn(add,node);
						//actionMessage = "";
					}
					else if(c!=null && !c.status()){
						this.switchOn(c,node);
					}
					else{
						actionMessage = actionMessage+": Error- Phone with id "+a+" already exists";
					}				
				}
				else{
					 actionMessage = actionMessage+": Error- No exchange with identifier "+b;
				}
				break;
			case "switchOffMobile":
				a = s.nextInt();
				MobilePhone delete = this.root.residentSet.FindPhone(a);
				if(delete != null){
					this.switchOff(delete);
					//actionMessage = "";
				}
				else{
					actionMessage = actionMessage+": Error- No mobile phone with identifier "+a;
				}
				break;
			case "queryNthChild":
				a = s.nextInt();
				b = s.nextInt();
				node = this.getNode(a);
				if(b<node.numChildren()){
					actionMessage = actionMessage + ": "+String.valueOf(node.child(b).identity); 
				}
				else{
					actionMessage = actionMessage+": Error- No "+ b +" child of Exchange "+a;
				}
				break;
			case "queryMobilePhoneSet":
				a = s.nextInt();
				node = this.getNode(a);
				actionMessage = actionMessage + ": "+node.residentSet.PrintSet();
				break;
			case "findPhone":
				a = s.nextInt();
				actionMessage = "queryFindPhone "+a;
				MobilePhone phone = this.root.residentSet.FindPhone(a);
				if(phone!=null){
					base = this.findPhone(phone);
					actionMessage = actionMessage +": "+base.identity;
				}
				else{
				    actionMessage = actionMessage+": Error - No mobile phone with identifier "+ a+" found in the network";
				}
				
				break;
			case "lowestRouter":
				a = s.nextInt();
				b = s.nextInt();
				
			    actionMessage = "queryLowestRouter "+a+" "+b;
				Exchange e1 = this.getNode(a);
				Exchange e2 = this.getNode(b);
				base = lowestRouter(e1,e2);
				actionMessage = actionMessage +": "+base.identity;
				break;
			case "findCallPath":
				a = s.nextInt();
				b = s.nextInt();
				actionMessage = "queryFindCallPath "+a+" "+b;
				MobilePhone m1 = this.root.residentSet.FindPhone(a);
				MobilePhone m2 = this.root.residentSet.FindPhone(b);
				
				if(m1 == null && m2==null){
					actionMessage = actionMessage +": Error - Mobile phones with identifier "+a+" and "+ b+" does not exist";
				}
				else if(m1 == null){
					actionMessage = actionMessage +": Error - Mobile phone with identifier "+a +" does not exist";
				}

				else if(m2 == null){
					actionMessage = actionMessage +": Error - Mobile phone with identifier "+b +" does not exist";
				}
				else if(m1.status() && m2.status()){
					ExchangeList list = routeCall(m1,m2);
					actionMessage = actionMessage +": "+ list.PrintList();
				}
				else if(!m1.status()){
				    actionMessage = actionMessage +": Error - Mobile phone with identifier "+a +" is currently switched off";
				}
				else if(!m2.status()){
				    actionMessage = actionMessage +": Error - Mobile phone with identifier "+b +" is currently switched off";
				}
				break;
			case "movePhone":
				a = s.nextInt();
				b = s.nextInt();
				MobilePhone p = this.root.residentSet.FindPhone(a);
				Exchange n = this.getNode(b);
				this.movePhone(p,n);
				break;
			}
		return( actionMessage);
	}
}
class Myset{
	public  linkedlist first;
	Myset(Object o){ 
		 first = new linkedlist(o);
	}					
	Myset(){ 
		 first = new linkedlist();
	}

	public Boolean IsEmpty(){
		if(this.first.IsEmpty()){
			return true;
		}
		else {return false;}
	}
	public Boolean IsMember(Object o){
		if(this.first.IsMember(o)){
			return true;
		}
		else{return false;}
	}
	public void Insert(Object o){
		if(!this.first.IsMember(o)){
			this.first.Insert(o);
		}
	}
	public void Delete(Object o){
		try{
			this.first.Delete(o);
		}
		catch(Exception e){

		}
	}
	public Myset Union(Myset a){
		Myset union = new Myset();
		linkedlist.node temp = this.first.head;
		for(int i=0;i<this.first.NumberOfMembers();i++){
			union.Insert(temp.data);
			temp = temp.next;
		}
		linkedlist extra = a.first;
		linkedlist.node temp1 = extra.head;
		for(int i=0;i<extra.NumberOfMembers();i++){
			if(!union.first.IsMember(temp1.data)){
				union.Insert(temp1.data);
			}
			temp1 = temp1.next;			
		}
		return union;
	}
	public Myset Intersection(Myset a){
		Myset intersection = new Myset();
		linkedlist.node temp = this.first.head;
		for(int i=0;i<this.first.NumberOfMembers();i++){
			if(this.IsMember(temp.data)&&a.IsMember(temp.data)){
				intersection.Insert(temp.data);
			}
			temp=temp.next;
		}
		return intersection;

	}
}
class linkedlist{
	public  node head;
	linkedlist(Object a){
		head = new node(a);
	}
	linkedlist(){
		head = null;
	}
	public class node{
		Object data;
		node next;
		node(){
			next = null;
		}
		node(Object a){
			data = a;
			next = null;
		}
	}
	node temp;
	
	public Boolean IsMember(Object o){
		temp = head;
		MobilePhone a = (MobilePhone)o;
		while(temp != null){
			MobilePhone temp2 =(MobilePhone)temp.data;
			if(temp2.number() == a.number()){return true;}
			temp = temp.next;			 
		}
		return false;
	}
	public Boolean IsEmpty(){
		if(head==null){
			return true;
		}
		else return false;
	}
	public void Insert(Object o){
		if(head == null){
			head = new node(o);
		}
		else{
			temp = head;
			while(temp.next!=null){
				temp = temp.next;
			}
			temp.next = new node(o);
		}
	}
	public void Delete(Object o) throws Exception{
		if(this.IsMember(o)){
			MobilePhone x = (MobilePhone)head.data;
			MobilePhone y  = (MobilePhone)o;
			if(x.number() == y.number()){
				this.head = head.next;
			}
			else{
				temp = head;
				MobilePhone temp2 = (MobilePhone)temp.next.data;
				while(temp2.number() != y.number()){
					temp = temp.next;					
					temp2 = (MobilePhone)temp.next.data;
				}
				temp.next= temp.next.next;
			}
		}
		else{
			throw new Exception("Object not found");
		}
	}
	public int NumberOfMembers(){
		temp = head;
		int count =0;
		while(temp!=null){
			count++;
			temp=temp.next;
		}
		return count;
	}	
}
class MobilePhone{
	private int Id;
	private Boolean Status = false;
	private Exchange Base = null;
	MobilePhone(int number){
		Id = number;
	}
	public int number(){
		return Id;
	}
	public Boolean status(){
		return Status;
	}
	public void switchOn(){
		Status=true;
	}
	public void switchOff(){
		Status=false;
	}
	public Exchange location(){
			if(this.Status){return Base;}
			else return null;
	}
	public void setExchange(Exchange b){
		this.Base = b;
	}
}
class MobilePhoneSet{
	Myset Phones; 
	MobilePhoneSet(MobilePhone P){
		 Phones=new Myset(P);
	}
	MobilePhoneSet(){
		Phones = new Myset();
	}
	public MobilePhone FindPhone(int a){
		
		linkedlist.node temp = Phones.first.head;
		while(temp!=null){
			MobilePhone temp2 = (MobilePhone)temp.data;
			if(temp2.number()==a){
				return temp2;
			}
			temp = temp.next;
		}
		return null;
	}
	public String PrintSet(){
		linkedlist.node temp = this.Phones.first.head;
		MobilePhone temp3 = null;		
		
		String result = new String("");
		while(temp!=null){
			MobilePhone temp2 = (MobilePhone)temp.data;	
			if(temp2.status()){
				result = result + temp2.number();
				if(temp.next != null){
					//temp3 = (MobilePhone)temp.next.data;
					//if(temp3.status())
						result = result + ", ";
				}
			}

			linkedlist.node pretemp = temp;
			temp3 = (MobilePhone)pretemp.data;
			temp = temp.next;
				
		}
		
		if(temp3!=null && !temp3.status()){
			String result1 = result.substring(0, result.length()-2);
					return result1;
		}
		return result;
	}
}
class ExchangeList{
	public  linkedlist list;

	ExchangeList(Exchange a){
		list = new linkedlist(a);
	}
	ExchangeList(){
		list = new linkedlist();
	}
	public String PrintList(){
		linkedlist.node temp = this.list.head;
		Exchange temp3 = null;		
		
		String result = new String("");
		while(temp!=null){
			Exchange temp2 = (Exchange)temp.data;	
			result = result + temp2.identity;
			if(temp.next != null){
		
				result = result + ", ";
			}
		
			temp = temp.next;
				
		}
		
		return result;
	}
}
class Exchange{
	int identity;
	Exchange parent;
	ExchangeList children;
	MobilePhoneSet residentSet;
	Exchange(int number){
		identity = number;
	}
	public Exchange parent(){
		return parent;
	}
	public int numChildren(){
		return children.list.NumberOfMembers();
	}
	public Exchange child(int i){
		if(this.children.list.head == null)
		 	return null;
		 linkedlist.node temp = this.children.list.head;

		for(int j=0;j<i;j++){
			temp = temp.next;
		}
		//System.out.println(temp.data.identity);
		return (Exchange)temp.data;
	}
	public Boolean isRoot(){
		if(this.identity ==  0){
			return true;
		}
		else return false;
	}
	public RoutingMapTree subtree(int i){
		RoutingMapTree subtree = new RoutingMapTree(this.child(i));
		return subtree;

	}
	public MobilePhoneSet residentSet(){
		return residentSet;
	}
}
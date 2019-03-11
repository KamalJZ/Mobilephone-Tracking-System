import java.util.*;

public class RoutingMapTree1{
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
		if(this.root.residentSet.Phones.IsMember(a)){
			Exchange c = a.location();
				while(!c.isRoot()){
					c.residentSet.Phones.Delete(a);
						c = c.parent();
				}
			c.residentSet.Phones.Delete(a);
		}
		if(!a.status()){
			a.switchOn();
			a.setExchange(b);
			Exchange temp = b;
			while(!temp.isRoot()){
				temp.residentSet.Phones.Insert(a);
				temp = temp.parent();
			}
			temp.residentSet.Phones.Insert(a);
		}
	}
	public void switchOff(MobilePhone a){
		
		/*	Exchange b = a.location();
			while(!b.isRoot()){
				b.residentSet.Phones.Delete(a);
					b = b.parent();
			}
			b.residentSet.Phones.Delete(a);
				
		*/	a.switchOff();
		
	}
	public String performAction(String actionMessage){
		int a,b;
		Exchange node;
		Scanner s = new Scanner(actionMessage);

		switch(s.next()){
			case "addExchange":
				a = s.nextInt();
				b = s.nextInt();
				if(this.containsNode(a)){
					Exchange parentnode = this.getNode(a);
					node = new Exchange(b);
					node.parent = 	parentnode;
					parentnode.children.Insert(node);
					node.residentSet = new MobilePhoneSet();
					node.children = new ExchangeList(); 
					//actionMessage = "";				
				}
				else{
					System.out.println("Error- No exchange with identifier "+a);
				}

				break;
			case "switchOnMobile":
				a = s.nextInt();
				b = s.nextInt();
				if(this.containsNode(b)){
					node = this.getNode(b);
					MobilePhone add = new MobilePhone(a);
					this.switchOn(add,node);
					//actionMessage = "";				
				}
				else{
					System.out.println("Error- No exchange with identifier "+b);
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
					System.out.println("Error- No mobile phone with identifier "+a);
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
					System.out.println("Error- No "+ b +" child of Exchange "+a);
				}
				break;
			case "queryMobilePhoneSet":
				a = s.nextInt();
				node = this.getNode(a);
				actionMessage = actionMessage + ": "+node.residentSet.Phones.first.PrintList();
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
		this.first.Delete(o);
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
		while(temp != null){
			if(temp.data == o){return true;}
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
	public void Delete(Object o){
		if(this.IsMember(o)){
			if(head.data == o){
				this.head = head.next;
			}
			else{
				temp = head;
				
				while(temp.next.data != o){
					temp = temp.next;
				}
				temp.next= temp.next.next;
			}
		}
	}
	public String PrintList(){
		temp = head;
		String result = new String("");
		while(temp!=null){
			MobilePhone temp2 = (MobilePhone)temp.data;
			if(temp2.status()){
				result = result + temp2.number();
				if(temp.next != null)
				result = result + ", ";
			}
			temp = temp.next;
			
		}
		return result;
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
	private Exchange Base;
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

}
 
class ExchangeList{
	public  node head;
	ExchangeList(Exchange a){
		head = new node(a);
	}
	ExchangeList(){
		head = null;
	}
	public class node{
		Exchange data;
		node next;
		node(){
			next = null;
		}
		node(Exchange a){
			data = a;
			next = null;
		}
	}
	node temp;
	
	public Boolean IsMember(Exchange o){
		temp = head;
		while(temp != null){
			if(temp.data == o){return true;}
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
	public void Insert(Exchange o){
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
	public void Delete(Exchange o){
		if(this.IsMember(o)){
			if(head.data == o){
				this.head = head.next;
			}
			else{
				temp = head;
				
				while(temp.next.data != o){
					temp = temp.next;
				}
				temp.next= temp.next.next;
			}
		}
	}
	public void PrintList(){
		temp = head;
		while(temp!=null){
			System.out.print(temp.data.identity + " ");
			temp = temp.next;
		}
	}
	public int NumberOfMembers(){
		if(head==null){
			return 0;
		}
		temp = head;
		int count =0;
		while(temp.next!=null){
			count++;
			temp=temp.next;
		}
		count++;
		return count;
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
		return children.NumberOfMembers();
	}
	public Exchange child(int i){
		if(this.children.head == null)
		 	return null;
		 ExchangeList.node temp = this.children.head;

		for(int j=0;j<i;j++){
			temp = temp.next;
		}
		//System.out.println(temp.data.identity);
		return temp.data;
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
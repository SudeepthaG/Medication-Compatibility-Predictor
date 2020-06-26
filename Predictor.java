import java.io.*;
import java.util.*;
//homework3
public class Predictor
{
    public static void main(String args[]) throws Exception {
        File input=new File("input.txt");
        FileWriter output=new FileWriter("output.txt");
        Scanner sc=new Scanner(input);
        int n=Integer.valueOf(sc.nextLine());
        LinkedList<String> query=new LinkedList<>();
        for(int i=0;i<n;i++)
            query.add(sc.nextLine());
        int k=Integer.valueOf(sc.nextLine());
        LinkedList<String> kb=new LinkedList<>();
        for(int i=0;i<k;i++)
            kb.add(sc.nextLine());

        Solution s=new Solution(n,query,k,kb);
        String result=s.solutionMainFuntion();
        System.out.println("Main function result"+result);
        output.write(result);
        output.close();
    }
}

class Solution
{
    int n;
    LinkedList<String> query;
    int k;
    LinkedList<String> kb;
    String result="";
    int std=0;
    public Solution(int n, LinkedList<String> query, int k, LinkedList<String> kb)
    {
        this.n=n;
        this.query=query;
        this.k=k;
        this.kb=kb;
    }


    List convertToCNF(LinkedList kb)
    {

        String sent;
        List<List> cnfkb=new LinkedList<>();
        String premise="";
//        System.out.println("literals befoore:"+literal);
//        System.out.println("cnfsent befoore:"+cnfsent);
        for(int i=0;i<kb.size();i++)
        {
            //System.out.println(itr.next());
            List<HashMap> cnfsent=new LinkedList<>();
            List<HashMap> cnfsentr=new LinkedList<>();
            sent= (String) kb.get(i);
//            System.out.println("sentence:"+sent);
//            System.out.println("literals start:"+literal);
//            System.out.println("cnfsent start:"+cnfsent);
            if(sent.contains("=>"))
            {
                String conclusion=sent.split("=>")[1].split("\\s")[1];
                premise=sent.split(" =>")[0];
                if(conclusion.contains("~"))
                {
                    HashMap<String,LinkedList> literal=new HashMap<>();
                    LinkedList<String> ll=new LinkedList<>();
                    ll.add("false");
                    String[] arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")")).split(",");
                    for(String a:arguments)
                        ll.add(a);
                    literal.put(conclusion.substring(0,conclusion.indexOf("(")).substring(1), ll);
                    cnfsent.add(literal);
                }
                else
                {
                    LinkedList<String> ll=new LinkedList<>();
                    HashMap<String,LinkedList> literal=new HashMap<>();
                    ll.add("true");
                    String[] arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")")).split(",");
                    for(String a:arguments)
                        ll.add(a);
                    literal.put(conclusion.substring(0,conclusion.indexOf("(")), ll);
                    cnfsent.add(literal);
                }
                if(premise.contains("&"))
                {
                    String[] premiseliterals=premise.split(" & ");
                    for(String pl:premiseliterals)
                    {
                        if(pl.contains("~"))
                        {
                            LinkedList<String> ll=new LinkedList<>();
                            HashMap<String,LinkedList> literal=new HashMap<>();
                            ll.add("true");
                            String[] arguments = pl.substring(pl.indexOf("(")+1,pl.indexOf(")")).split(",");
                            for(String a:arguments)
                                ll.add(a);
                            literal.put(pl.substring(0,pl.indexOf("(")).substring(1), ll);
                            cnfsent.add(literal);
                        }
                        else
                        {
                            LinkedList<String> ll=new LinkedList<>();
                            HashMap<String,LinkedList> literal=new HashMap<>();
                            ll.add("false");
                            String[] arguments = pl.substring(pl.indexOf("(")+1,pl.indexOf(")")).split(",");
                            for(String a:arguments)
                                ll.add(a);
                            literal.put(pl.substring(0,pl.indexOf("(")), ll);
                            cnfsent.add(literal);
                        }
                    }
                }
                else
                {
                    if(premise.contains("~"))
                    {
                        LinkedList<String> ll=new LinkedList<>();
                        HashMap<String,LinkedList> literal=new HashMap<>();
                        ll.add("true");
                        String[] arguments = premise.substring(premise.indexOf("(")+1,premise.indexOf(")")).split(",");
                        for(String a:arguments)
                            ll.add(a);
                        literal.put(premise.substring(0,premise.indexOf("(")).substring(1), ll);
                        cnfsent.add(literal);
                    }
                    else
                    {
                        LinkedList<String> ll=new LinkedList<>();
                        HashMap<String,LinkedList> literal=new HashMap<>();
                        ll.add("false");
                        String[] arguments = premise.substring(premise.indexOf("(")+1,premise.indexOf(")")).split(",");
                        for(String a:arguments)
                            ll.add(a);
                        literal.put(premise.substring(0,premise.indexOf("(")), ll);
                        cnfsent.add(literal);
                    }
                }
            }
            else
            {
                String conclusion = sent;
                if(conclusion.contains("~"))
                {
                    LinkedList<String> ll=new LinkedList<>();
                    HashMap<String,LinkedList> literal=new HashMap<>();
                    ll.add("false");
                    String[] arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")")).split(",");
                    for(String a:arguments)
                        ll.add(a);
                    literal.put(conclusion.substring(0,conclusion.indexOf("(")).substring(1), ll);
                    cnfsent.add(literal);
                }
                else
                {
                    LinkedList<String> ll=new LinkedList<>();
                    HashMap<String,LinkedList> literal=new HashMap<>();
                    ll.add("true");
                    String[] arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")")).split(",");
                    for(String a:arguments)
                        ll.add(a);
                    literal.put(conclusion.substring(0,conclusion.indexOf("(")), ll);
                    cnfsent.add(literal);
                }
            }

            //standardizing variables
            HashMap<String,String> replacehm=new HashMap<>();
            HashMap<String,LinkedList> literalr=new HashMap<>();
            LinkedList<String> llr=new LinkedList<>();
            for(int ir=0;ir<cnfsent.size();ir++)
            {
                literalr=cnfsent.get(ir);
                llr=literalr.get(literalr.keySet().toArray()[0]);
//                System.out.println(literalr);
//                System.out.println(llr);
                for(int jr=1;jr<llr.size();jr++)
                {
                    if(Character.isLowerCase(llr.get(jr).charAt(0)))
                    {
                        if(!replacehm.containsKey(llr.get(jr)))
                        {
                            replacehm.put(llr.get(jr),llr.get(jr)+""+std);
                            std++;
                        }
                    }
                }
            }
//            System.out.println(replacehm);
//            System.out.println(cnfsent);
            for(int ir=0;ir<cnfsent.size();ir++)
            {
                literalr=cnfsent.get(ir);
//                System.out.println(literalr);
                llr=literalr.get(literalr.keySet().toArray()[0]);
                for(int kr=0;kr<llr.size();kr++)
                {
                    for (int jr = 0; jr < replacehm.size(); jr++)
                    {
                        Collections.replaceAll(llr, (String) replacehm.keySet().toArray()[jr], replacehm.get(replacehm.keySet().toArray()[jr]));
                        literalr.put((String) literalr.keySet().toArray()[0], llr);
                    }
                }
//                System.out.println(literalr);
            }
            cnfsentr.add(literalr);
//            System.out.println(cnfsentr);
            cnfkb.add(cnfsent);
//            System.out.println("literals:"+literal);
//            System.out.println("cnfsent:"+cnfsent);
        }

        return cnfkb;
    }
    
    private static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String solutionMainFuntion()
    {
        String result="";
        List cnfkb = convertToCNF(kb);
//        System.out.println("cnfkb:"+cnfkb);
        for(String q:query)
        {
            String qresult="FALSE\n";
            List<List> currentkb=new LinkedList<>();
            currentkb=convertToCNF(kb);
            List<HashMap> cnfsent=new LinkedList<>();
            List<HashMap> cnfsent2=new LinkedList<>();
            if(q.contains("~"))
            {
                LinkedList<String> ll=new LinkedList<>();
                HashMap<String,LinkedList> literal=new HashMap<>();
                LinkedList<String> ll2=new LinkedList<>();
                HashMap<String,LinkedList> literal2=new HashMap<>();
                ll.add("true");
                ll2.add("false");
                String[] arguments = q.substring(q.indexOf("(")+1,q.indexOf(")")).split(",");
                for(String a:arguments)
                {
                    ll.add(a);
                    ll2.add(a);
                }
                for(String a:arguments)
                {
                    String replacing=a+""+std;
                    if(Character.isLowerCase(a.charAt(0)))
                    {
                        Collections.replaceAll(ll, a, replacing);
                        Collections.replaceAll(ll2, a, replacing);
                        std++;
                    }

                }
                literal.put(q.substring(0,q.indexOf("(")).substring(1), ll);
                cnfsent.add(literal);
                literal2.put(q.substring(0,q.indexOf("(")).substring(1), ll2);
                cnfsent2.add(literal2);
            }
            else
            {
                LinkedList<String> ll=new LinkedList<>();
                HashMap<String,LinkedList> literal=new HashMap<>();
                LinkedList<String> ll2=new LinkedList<>();
                HashMap<String,LinkedList> literal2=new HashMap<>();
                ll.add("false");
                ll2.add("true");
                String[] arguments = q.substring(q.indexOf("(")+1,q.indexOf(")")).split(",");
                for(String a:arguments)
                {
                    ll.add(a);
                    ll2.add(a);
                }
                for(String a:arguments)
                {
                    String replacing = a + "" + std;
                    if (Character.isLowerCase(a.charAt(0))) {
                        Collections.replaceAll(ll, a, replacing);
                        Collections.replaceAll(ll2, a, replacing);
                        std++;
                    }
                }
                literal.put(q.substring(0,q.indexOf("(")), ll);
                literal2.put(q.substring(0,q.indexOf("(")), ll2);
                cnfsent.add(literal);
                cnfsent2.add(literal2);
            }
//            System.out.println("negation Query:"+cnfsent);
//            System.out.println("query"+cnfsent2);
            currentkb.add(cnfsent);
            List<List> cnfsent2kb=new LinkedList<>();
            cnfsent2kb.add(cnfsent2);
//            System.out.println("\n\ncurrentkbfor next query:"+currentkb);
            int counter=0;
            int i=0,p=0;
            boolean loopbreak=false;
            boolean addition=false;
            for(i=0;i<currentkb.size();i++) {

                if(addition)
                {
                    i=0;
                }
                addition=false;


                List<List> sent_i = currentkb.get(i);
                //System.out.println("\ni="+i+" list="+sent_i);
                for (p = 0; p < currentkb.size(); p++) {
                    List<List> sent_p = currentkb.get(p);
                    //  System.out.println("\np="+p+" list="+sent_p);

                    for (int j = 0; j < sent_i.size(); j++) {
                        HashMap<String, LinkedList> predicate = (HashMap<String, LinkedList>) sent_i.get(j);
//                        System.out.println("j=" + j + " hashmap=" + predicate);
                        for (int qq = 0; qq < sent_p.size(); qq++) {
                            HashMap<String, LinkedList> predicatep = (HashMap<String, LinkedList>) sent_p.get(qq);
                            if ((predicate.keySet()).equals(predicatep.keySet())) {
                                if (!((predicate.get(predicate.keySet().toArray()[0]).get(0)).equals(predicatep.get(predicatep.keySet().toArray()[0]).get(0))))
                                {
                                    List res=unification(sent_i, sent_p, predicate, predicatep);
                                    HashMap<String,String> hm=new HashMap<>();
                                    hm.put("true","true");
                                    List<HashMap> resll=new LinkedList<>();
                                    resll.add(hm);
                                    if(res.contains(hm))
                                    {
                                        qresult="TRUE\n";
                                        loopbreak=true;
                                        break;
                                    }
                                    else if(res.size()!=0)
                                    {

                                        List<List> resultlist=new LinkedList<>();
                                        resultlist.add(res);
                                        List<List> resultlist2=new LinkedList<>();
                                        resultlist2=(List) deepCopy(resultlist);
                                        if(resultlist2.equals(cnfsent2kb))
                                        {

//                                            System.out.println("negation Query stmt inferred.hece true");
                                            qresult="TRUE\n";
                                            loopbreak=true;
                                            break;
                                        }
                                        if(currentkb.contains(resultlist2)&&resultlist2.equals(cnfsent))
                                        {
//                                            System.out.println("Query stmt inferred.hece false");
                                            loopbreak=true;
                                            break;
                                        }

                                        HashMap<String,String> hmt=new HashMap<>();
                                        hmt.put("false","false");
                                        List<HashMap> resllt=new LinkedList<>();
                                        resllt.add(hmt);


                                        if(!currentkb.contains(res)&&!res.contains(hmt))
                                        {
                                            currentkb.addAll(resultlist2);
                                            addition=true;
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                        if(loopbreak==true)
                            break;
                    }
                    if(loopbreak==true)
                        break;
                }
                if(loopbreak==true)
                    break;
            }
            result=result+qresult;
        }
        return result;
    }

    List unification(List<List> sent_i1, List<List> sent_p1, HashMap<String, LinkedList> predicate1, HashMap<String, LinkedList> predicatep1)
    {
        List<List> sent_i=new LinkedList<>();
        sent_i=(List)deepCopy(sent_i1);
        List<List> sent_p=new LinkedList<>();
        sent_p=(List)deepCopy(sent_p1);
        HashMap<String, LinkedList> predicate=new HashMap<>();
        predicate.putAll(predicate1);
        HashMap<String, LinkedList> predicatep=new HashMap<>();
        predicatep.putAll(predicatep1);
        LinkedList<String> a = new LinkedList<>();
        a = (LinkedList<String>) deepCopy(predicate.get(predicate.keySet().toArray()[0]));
        LinkedList b = new LinkedList<>();
        b=(LinkedList<String>)deepCopy(predicatep.get(predicatep.keySet().toArray()[0]));
        List<HashMap> newsent = new LinkedList<>();
        HashMap<String,String> substitution = new HashMap<>();
        a.remove(0);
        b.remove(0);
        substitution.putAll(unify(a, b, substitution));
        if(substitution.size()==0&&a.equals(b))
        {
            //trying resolution for given sentences and retruning resolved sentence if possible or else empty sentence

            List<HashMap> newsentx = new LinkedList<>();
            for(int ix=0;ix<sent_i.size();ix++)
            {
                HashMap<String, LinkedList> p = (HashMap<String, LinkedList>) sent_i.get(ix);
                newsentx.add(p);
            }
            for(int ix=0;ix<sent_p.size();ix++)
            {
                HashMap<String, LinkedList> p = (HashMap<String, LinkedList>) sent_p.get(ix);
                newsentx.add(p);
            }
            //checking resolution possible for above newsentx
            int counter=0,i=0,j=0;
            boolean countx=false;
            for(i=0;i<newsentx.size();i++) {
                HashMap<String, LinkedList> hm1 = new HashMap<>();
                hm1.putAll(newsentx.get(i));
                for (j = 0; j < newsentx.size(); j++) {
                    HashMap<String, LinkedList> hm2 = new HashMap<>();
                    hm2.putAll(newsentx.get(j));
                    counter = 0;
                    if (((newsentx.get(i).keySet().toArray()[0]).equals(newsentx.get(j).keySet().toArray()[0]))) {
                        LinkedList<String> ll1 = new LinkedList<>();
                        ll1 = (LinkedList) deepCopy(hm1.get(hm1.keySet().toArray()[0]));
                        LinkedList ll2 = new LinkedList<>();
                        ll2 = (LinkedList) deepCopy(hm2.get(hm2.keySet().toArray()[0]));
                        if (ll1.size() == ll2.size())
                            counter++;
                        if (counter == 1) {
                            if (!(ll1.get(0).equals(ll2.get(0))))
                                counter++;
                            int z = 0;
                            for (z = 1; z < ll1.size(); z++) {
                                if (!ll1.get(z).equals(ll2.get(z)))
                                    break;
                            }
                            if (z == ll1.size())
                                counter++;
                        }
                        if (counter == 3) {
//                            System.out.println("Resolution possible" + newsentx.get(i).keySet().toArray()[0] + " with " + newsentx.get(j).keySet().toArray()[0]);
                            HashMap<String, List> rm = new HashMap<>();
                            newsentx.remove(hm1);
                            newsentx.remove(hm2);
                            countx = true;
                            break;
                        }

                    }
                }
            }
            if(countx==true)
                return newsentx;
            else
                return newsent;
        }
        HashMap<Integer, Integer> failure=new HashMap<>();
        failure.put(0,0);
        if (substitution.equals(failure))
            return newsent;

        //Substituting the variables with values
        for(Map.Entry<String,String> sub:substitution.entrySet())
        {
            for (int j = 0; j < sent_i.size(); j++)
            {
                HashMap<String, LinkedList> p = (HashMap<String, LinkedList>) sent_i.get(j);
                String pkey= (String) p.keySet().toArray()[0];
                LinkedList<String> values=new LinkedList<>();
                values=(LinkedList)deepCopy(p.get(pkey));
                Collections.replaceAll(values,sub.getKey(),sub.getValue());
                p.put(pkey,values);
                if(!newsent.contains(p))
                    newsent.add(p);
            }
            for (int j = 0; j < sent_p.size(); j++)
            {
                HashMap<String, LinkedList> p = (HashMap<String, LinkedList>) sent_p.get(j);
                String pkey= (String) p.keySet().toArray()[0];
                LinkedList<String> values=new LinkedList<>();
                values=(LinkedList)deepCopy(p.get(pkey));
                Collections.replaceAll(values,sub.getKey(),sub.getValue());
                p.put(pkey,values);
                if(!newsent.contains(p))
                    newsent.add(p);
            }
//            System.out.println("new sentence"+newsent);
        }
        substitution.clear();

        boolean tautology=false;
        
        //Resolution for tautology
        if(!tautology) {
            int counter = 0, i = 0, j = 0;
            for (i = 0; i < newsent.size(); i++) {
                HashMap<String, LinkedList> hm1 = new HashMap<>();
                hm1.putAll(newsent.get(i));
                for (j = 0; j < newsent.size(); j++) {
                    HashMap<String, LinkedList> hm2 = new HashMap<>();
                    hm2.putAll(newsent.get(j));
                    counter = 0;
                    if (((newsent.get(i).keySet().toArray()[0]).equals(newsent.get(j).keySet().toArray()[0]))) {
                        LinkedList<String> ll1 = new LinkedList<>();
                        ll1 = (LinkedList) deepCopy(hm1.get(hm1.keySet().toArray()[0]));
                        LinkedList ll2 = new LinkedList<>();
                        ll2 = (LinkedList) deepCopy(hm2.get(hm2.keySet().toArray()[0]));
                        if (ll1.size() == ll2.size())
                            counter++;
                        if (counter == 1) {
                            if (!(ll1.get(0).equals(ll2.get(0))))

                                counter++;
                            int z = 0;
                            for (z = 1; z < ll1.size(); z++) {
                                if (!ll1.get(z).equals(ll2.get(z)))
                                    break;
                            }
                            if (z == ll1.size())
                                counter++;
                        }
                        if (counter == 3) {

                            //System.out.println("Resolution possible"+newsent.get(i).keySet().toArray()[0]+" with "+newsent.get(j).keySet().toArray()[0]);
                            HashMap<String, List> rm = new HashMap<>();
                            newsent.remove(hm1);
                            newsent.remove(hm2);
                            break;
                        }
                    }
                }
                if (counter == 3)
                    break;
            }
        }




        //checking for tautology in resultant  solution
        int counter = 0, i = 0, j = 0;
        for (i = 0; i < newsent.size(); i++) {
            HashMap<String, LinkedList> hm1 = new HashMap<>();
            hm1.putAll(newsent.get(i));

            for (j = 0; j < newsent.size(); j++) {
                HashMap<String, LinkedList> hm2 = new HashMap<>();
                hm2.putAll(newsent.get(j));
                counter = 0;
                if (((newsent.get(i).keySet().toArray()[0]).equals(newsent.get(j).keySet().toArray()[0]))) {
                    LinkedList<String> ll1 = new LinkedList<>();
                    ll1 = (LinkedList) deepCopy(hm1.get(hm1.keySet().toArray()[0]));
                    LinkedList ll2 = new LinkedList<>();
                    ll2 = (LinkedList) deepCopy(hm2.get(hm2.keySet().toArray()[0]));
                    if (ll1.size() == ll2.size())
                        counter++;
                    if (counter == 1) {
                        //                        System.out.println("Comparing opposite values"+ll1.get(0)+" equals "+ll2.get(0)+"="+!(ll1.get(0).equals(ll2.get(0))));
                        if (!(ll1.get(0).equals(ll2.get(0))))

                            counter++;
                        int z = 0;
                        for (z = 1; z < ll1.size(); z++) {
                            if (!ll1.get(z).equals(ll2.get(z)))
                                break;
                        }
                        if (z == ll1.size())
                            counter++;
                    }
                    if (counter == 3) {
                        tautology=true;
                        break;
                    }
                }
            }
            if (counter == 3)
                break;
        }
        if(newsent.size()==0)
        {
            HashMap<String,String> hm=new HashMap<>();
            hm.put("true","true");
            newsent.add(hm);
        }
        if(tautology)
        {
            HashMap<String,String> hm=new HashMap<>();
            hm.put("false","false");
            newsent.clear();
            newsent.add(hm);
        }

        return newsent;
    }

    HashMap unify(LinkedList x, LinkedList y,HashMap<String,String> theta1)
    {
        HashMap<String,String> theta=new HashMap<>();
        theta=(HashMap)deepCopy(theta1);
        HashMap<Integer, Integer> failure=new HashMap<>();
        failure.put(0,0);
        int varx=0;
        int vary=0;
        String value= (String) x.get(0);
        String a="aa";
        String b="aa";
        if(x.size()==1)
        {
            a= (String) x.get(0);
            varx=1;
        }
        if(y.size()==1)
        {
            b= (String) y.get(0);
            vary=1;
        }
        if(theta.equals(failure))
        {
//            System.out.println("Returning failure");
            return failure;
        }
        else if(Character.isUpperCase(a.charAt(0))&&Character.isUpperCase(b.charAt(0)))
        {
            if(a.equals(b))
            {
                theta.put(a, b);
                return theta;
            }
            else
                return failure;
        }
        else if(x.equals(y))
            return theta;
        else if(varx==1&&(Character.isLowerCase(a.charAt(0))))
            return unifyVar(a,b,theta);
        else if(vary==1&&(Character.isLowerCase(b.charAt(0))))
            return unifyVar(b,a,theta);
        else if(x instanceof LinkedList && y instanceof LinkedList)
        {
            LinkedList<String> valx=new LinkedList<>();
            valx.add((String) x.get(0));
            LinkedList<String> valy=new LinkedList<>();
            valy.add((String) y.get(0));
            x.remove(0);
            y.remove(0);
            return unify(x,y,unify(valx,valy, theta));
        }
        else
            return failure;
    }

    HashMap unifyVar(String a, String b, HashMap theta)
    {
        LinkedList lla=new LinkedList();
        lla.add(a);
        LinkedList llb=new LinkedList();
        llb.add(b);
        HashMap<Integer, Integer> failure=new HashMap<>();
        failure.put(0,0);
        if(theta.containsKey(a))
        {
            LinkedList<String> a_value=new LinkedList<>();
            a_value.add((String) theta.get(a));
            return unify(a_value, llb, theta);
        }
        else if(theta.containsKey(b))
        {
            LinkedList<String> b_value=new LinkedList<>();
            b_value.add((String) theta.get(b));
            return unify(lla, b_value, theta);
        }
        else
        {
            theta.put(a,b);
            return theta;
        }
    }
}

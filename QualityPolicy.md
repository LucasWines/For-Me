### Quality Policy
> Describe your Quality Policy in detail for this Sprint (remember what I ask you to do when I talk about the "In your Project" part in the lectures and what is mentioned after each assignment (in due course you will need to fill out all of them, check which ones are needed for each Deliverable). You should keep adding things to this file and adjusting your policy as you go.
> Check in Project: Module Concepts document on Canvas in the Project module for more details 

**GitHub Workflow** (start Sprint 1)
  > Describe your Workflow

**Unit Tests Blackbox** (start Sprint 2)
  > Describe your Blackbox testing policy
     - Before a push to master each team member must test the program.  

 **Unit Tests Whitebox** (online: start Sprint 2, campus: start Sprint 3)
  > Describe your Whitebox testing policy
    - We will choose a team member to quickly check over our code after each commit.   

**Code Review** (online: start Sprint 2, campus: start Sprint 2)
  > Describe your Code Review policy for on campus it is ok to have a less formal process in Sprint 2, should be updated in Sprint 3 though
   - We will have a meeting with the team member chosen as the reviewer to discuss code.  

  > Include a checklist/questions list which every developer will need to fill out/answe when creating a Pull Request to the Dev branch.
    - First question: Do naming conventions follow given coding standards?
    - Second question: Are all new attributes private?
    - Third question: Are all literal values, except loop indices starting at 0 or 1, declared as constants?
    - Fourth question: Is all code stylistically consistent?

  > Include a checklist/question list which every reviewer will need to fill out/anser when conducting a review, this checklist (and the answers of course) need to be put into the Pull Request review.
    - Do naming conventions follow given coding standards?
    - Are all new attributes private?
    - Are all literal values, except loop indices starting at 0 or 1, declared as constants?
    - Is all code stylistically consistent?
    - Code Smells:
        - Is there any duplicate code?
        - Are any classes too large?
        - Are there switch statements?
        - Is there a class or classes that uses the methods of another class too extensively?
        - Is there a class or classes that uses too little?
        - Is there an excessive use of literals?
        - Are there any groups of variables passed around together?
        - Do any methods have too many parameters?
        - Are any methods overly long?
        - Are all identifiers an adequate length (not too long or short)?
    - Are there any new functional defects?

**Static Analysis**  (online: start Sprint 3, campus: start Sprint 3)
  > We will make sure all new code complies with all Static Analysis testing methods before a pull request.   

**Continuous Integration**  (start Sprint 3, campus: start Sprint 3)
  > We will set up continuous integration whenever we push to github.

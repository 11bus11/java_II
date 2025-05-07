package library;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Loan {
    int loanID;
    LocalDateTime borrowDate;
    User user;
    ArrayList<Copy> copiesLoaned;

    public Loan(int loanID, LocalDateTime borrowDate, User user, ArrayList<Copy> copiesLoaned) {
        this.loanID = loanID;
        this.borrowDate = LocalDateTime.now();
        this.user = user;
        this.copiesLoaned = copiesLoaned;
    }

    
}

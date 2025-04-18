# 🚀 PaisehPay - Expense Splitting Made Easy  

**PaisehPay** is an Android app that simplifies splitting expenses among friends and groups, with real-time updates and automatic debt calculations. Built with **Java, Firebase Realtime Database, and Firebase Authentication**.  

---

## 📥 Installation & Setup  

### **Prerequisites**  
- Android Studio (latest version)  
- Firebase account (free tier)  
- Android device/emulator (API 26+)  

### **1. Clone the Repository**  
```bash  
git clone https://github.com/NoelRook/PaisehPay.git  
```  

### **2. Connect to Your Firebase Project**  
1. Go to the [Firebase Console](https://console.firebase.google.com/).  
2. Create a new project (e.g., `PaisehPay`).  
3. **Enable Authentication**:  
   - Go to **Authentication → Sign-in Method** → Enable **Email/Password** and **Google Sign-In**.  
4. **Set Up Realtime Database**:  
   - Go to **Realtime Database → Create Database** (Start in **test mode** for development).  
   - Add these security rules:  
     ```json  
     {
       "rules": {
         "users": {
           "$uid": {
             ".read": "auth != null && auth.uid == $uid",
             ".write": "auth != null && auth.uid == $uid"
           }
         },
         "groups": {
           "$groupId": {
             ".read": "auth != null && data.child('members').hasChild(auth.uid)",
             ".write": "auth != null && data.child('members').hasChild(auth.uid)"
           }
         }
       }
     }
     ```  
5. **Register Your Android App**:  
   - Click **Project Settings → Add App → Android**.  
   - Package name: `com.example.paisehpay` (or update it in `build.gradle`).  
   - Download `google-services.json` and place it in `app/`.  

### **3. Build & Run**  
- Open the project in Android Studio.  
- Sync Gradle and run on an emulator/device.  

---

## 🔥 Key Features  

### **1. Real-Time Expense Tracking**  
- Instantly sync expenses across all group members.  
- **Tech**: Firebase Realtime Database listeners.  

### **2. Automated Debt Calculations**  
- Calculates "who owes whom" using optimized algorithms.  
- **Tech**: `DebtCalculator.java` with `HashMap` aggregation.  

### **3. Secure Authentication**  
- Sign in via **Email/Password** or **Google**.  
- **Tech**: Firebase Auth + JWT token validation.  

### **4. Offline Support**  
- Works without internet; syncs when reconnected.  
- **Tech**: Firebase’s `keepSynced(true)`.  

### **5. Dynamic Group Management**  
- Create groups, add members, and assign expenses.  
- **Tech**: RecyclerView + Firebase adapters.  

### **6. Tax & Service Charge Support**  
- Auto-adds GST (9%) or SVC (10%) to expenses.  
- **Tech**: `Nine_GST.java`, `Ten_SVC.java` calculators.  

---

## 🛠 Technical Highlights  

### **Modular Architecture**  
| Layer              | Key Components                          |  
|--------------------|----------------------------------------|  
| **UI**             | Activities, Fragments, RecyclerViews   |  
| **Business Logic** | `DebtCalculator`, `ExpenseSingleton`   |  
| **Data Access**    | `FirebaseDatabaseAdapter`, `UserAdapter` |  
| **Infrastructure** | Firebase Auth, Realtime Database       |  

### **OOP Principles Applied**  
- **Abstraction**: `BaseDatabaseOperations` interface.  
- **Encapsulation**: Private fields + getters/setters in `Expense.java`.  
- **Inheritance**: `Transaction` → `Expense`/`Settlement`.  
- **Polymorphism**: Generic `FirebaseDatabaseAdapter<T>`.  

---

## 📈 Future Roadmap  
- [ ] **Multi-Currency Support**: For international groups.  
- [ ] **Push Notifications**: Remind users of pending debts.  

---

## 🚨 Troubleshooting  
- **"Missing google-services.json"**: Ensure the file is in `app/` and the package name matches.  
- **Authentication Failures**: Check Firebase Auth settings (e.g., enabled providers).  
- **Database Permission Denied**: Verify security rules in Firebase Console.  

---

## 📜 License  
MIT © [NoelRook](https://github.com/NoelRook)  

---

💡 **Pro Tip**: Use the `Debug` build variant to see detailed Firebase operation logs!  

--- 

Let us know if you’d like to contribute or report issues [here](https://github.com/NoelRook/PaisehPay/issues). Happy splitting! 🎉

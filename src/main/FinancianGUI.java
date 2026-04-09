package main;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.EMIDAO;
import dao.LoanDAO;
import model.Account;
import model.Customer;
import model.EMI;
import model.Loan;
import util.DBConnection;
import util.FinancianException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * FinancianGUI - Swing frontend for the FINANCIAN application.
 * Drop this file into src/main/ and compile + run same as MainMenu.
 *
 * Compile:
 *   javac -cp lib/mysql-connector-j-9.6.0.jar -d out $(find src -name "*.java")
 * Run:
 *   java -cp out:lib/mysql-connector-j-9.6.0.jar main.FinancianGUI
 */
public class FinancianGUI extends JFrame {

    // ── DAOs ──────────────────────────────────────────────────────────
    static CustomerDAO customerDAO = new CustomerDAO();
    static AccountDAO  accountDAO  = new AccountDAO();
    static LoanDAO     loanDAO     = new LoanDAO();
    static EMIDAO      emiDAO      = new EMIDAO();

    // ── Palette ───────────────────────────────────────────────────────
    static final Color BG_DARK     = new Color(13, 17, 23);
    static final Color BG_PANEL    = new Color(22, 27, 34);
    static final Color BG_CARD     = new Color(30, 37, 46);
    static final Color ACCENT      = new Color(88, 166, 255);
    static final Color ACCENT2     = new Color(63, 185, 80);
    static final Color ACCENT_WARN = new Color(255, 166, 0);
    static final Color ACCENT_RED  = new Color(248, 81, 73);
    static final Color TEXT_MAIN   = new Color(230, 237, 243);
    static final Color TEXT_MUTED  = new Color(125, 140, 158);
    static final Color BORDER_CLR  = new Color(48, 54, 61);

    // ── Fonts ─────────────────────────────────────────────────────────
    static final Font FONT_TITLE  = new Font("Monospaced", Font.BOLD, 13);
    static final Font FONT_LABEL  = new Font("Monospaced", Font.PLAIN, 12);
    static final Font FONT_SMALL  = new Font("Monospaced", Font.PLAIN, 11);
    static final Font FONT_HEADER = new Font("Monospaced", Font.BOLD, 15);

    // ── Layout ────────────────────────────────────────────────────────
    JPanel   sideBar;
    JPanel   contentArea;
    JLabel   statusBar;
    CardLayout cardLayout;
    JPanel   cards;

    public FinancianGUI() {
        super("FINANCIAN — EMI Financial Stress & Early Warning System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
                dispose();
                System.exit(0);
            }
        });
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        buildUI();
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────────────
    //  MAIN LAYOUT
    // ─────────────────────────────────────────────────────────────────
    void buildUI() {
        setLayout(new BorderLayout(0, 0));

        // Top header bar
        add(buildHeader(), BorderLayout.NORTH);

        // Sidebar
        sideBar = buildSidebar();
        add(sideBar, BorderLayout.WEST);

        // Content cards
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBackground(BG_DARK);
        cards.add(buildDashboard(),         "dashboard");
        cards.add(buildCustomerPanel(),     "customer");
        cards.add(buildAccountPanel(),      "account");
        cards.add(buildLoanPanel(),         "loan");
        cards.add(buildEMIPanel(),          "emi");
        cards.add(buildReportsPanel(),      "reports");
        add(cards, BorderLayout.CENTER);

        // Status bar
        statusBar = new JLabel("  Ready.");
        statusBar.setFont(FONT_SMALL);
        statusBar.setForeground(TEXT_MUTED);
        statusBar.setBackground(BG_PANEL);
        statusBar.setOpaque(true);
        statusBar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_CLR));
        statusBar.setPreferredSize(new Dimension(0, 24));
        add(statusBar, BorderLayout.SOUTH);
    }

    JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_PANEL);
        h.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        h.setPreferredSize(new Dimension(0, 48));

        JLabel logo = new JLabel("  ◈ FINANCIAN");
        logo.setFont(new Font("Monospaced", Font.BOLD, 16));
        logo.setForeground(ACCENT);

        JLabel sub = new JLabel("DBMS Mini Project — Second Year CSE  ");
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_MUTED);

        h.add(logo, BorderLayout.WEST);
        h.add(sub,  BorderLayout.EAST);
        return h;
    }

    JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(BG_PANEL);
        side.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_CLR));
        side.setPreferredSize(new Dimension(200, 0));

        side.add(Box.createVerticalStrut(16));
        side.add(sideLabel("NAVIGATION"));
        side.add(Box.createVerticalStrut(4));
        side.add(sideBtn("⌂  Dashboard",  "dashboard"));
        side.add(sideLabel("MODULES"));
        side.add(sideBtn("👤  Customer",   "customer"));
        side.add(sideBtn("🏦  Account",    "account"));
        side.add(sideBtn("💳  Loan",       "loan"));
        side.add(sideBtn("📅  EMI",        "emi"));
        side.add(sideLabel("ANALYTICS"));
        side.add(sideBtn("📊  Reports",    "reports"));
        side.add(Box.createVerticalGlue());
        return side;
    }

    JLabel sideLabel(String text) {
        JLabel l = new JLabel("  " + text);
        l.setFont(new Font("Monospaced", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(200, 28));
        l.setBorder(new EmptyBorder(12, 0, 2, 0));
        return l;
    }

    JButton sideBtn(String text, String card) {
        JButton b = new JButton(text);
        b.setFont(FONT_LABEL);
        b.setForeground(TEXT_MAIN);
        b.setBackground(BG_PANEL);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(200, 36));
        b.setBorder(new EmptyBorder(6, 16, 6, 8));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(BG_CARD); }
            public void mouseExited(MouseEvent e)  { b.setBackground(BG_PANEL); }
        });
        b.addActionListener(e -> { cardLayout.show(cards, card); status("Switched to: " + text.trim()); });
        return b;
    }

    // ─────────────────────────────────────────────────────────────────
    //  DASHBOARD
    // ─────────────────────────────────────────────────────────────────
    JPanel buildDashboard() {
        JPanel p = darkPanel(new BorderLayout(16, 16));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Monospaced", Font.BOLD, 20));
        title.setForeground(TEXT_MAIN);
        p.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        grid.add(statCard("Customers",  "👤", ACCENT,      () -> customerDAO.getAll().size()));
        grid.add(statCard("Accounts",   "🏦", ACCENT2,     () -> accountDAO.getAll().size()));
        grid.add(statCard("Loans",      "💳", ACCENT_WARN, () -> loanDAO.getAll().size()));
        grid.add(quickCard("Add Customer",  "customer"));
        grid.add(quickCard("View EMI Risk", "reports"));
        grid.add(quickCard("Overdue EMIs",  "reports"));

        p.add(grid, BorderLayout.CENTER);

        JLabel hint = new JLabel("Use the sidebar to navigate between modules.");
        hint.setFont(FONT_SMALL);
        hint.setForeground(TEXT_MUTED);
        p.add(hint, BorderLayout.SOUTH);
        return p;
    }

    JPanel statCard(String label, String icon, Color accent, StatSupplier supplier) {
        JPanel c = card();
        c.setLayout(new BorderLayout(8, 8));
        c.setBorder(new CompoundBorder(
            new MatteBorder(0, 3, 0, 0, accent),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel ico = new JLabel(icon + "  " + label);
        ico.setFont(FONT_LABEL);
        ico.setForeground(TEXT_MUTED);

        JLabel val = new JLabel("...");
        val.setFont(new Font("Monospaced", Font.BOLD, 28));
        val.setForeground(accent);

        c.add(ico, BorderLayout.NORTH);
        c.add(val, BorderLayout.CENTER);

        // Load count in background
        SwingWorker<Integer, Void> w = new SwingWorker<>() {
            protected Integer doInBackground() { try { return supplier.get(); } catch (Exception e) { return -1; } }
            protected void done() { try { val.setText(String.valueOf(get())); } catch (Exception ignored) {} }
        };
        w.execute();
        return c;
    }

    JPanel quickCard(String text, String card) {
        JPanel c = card();
        c.setLayout(new GridBagLayout());
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel l = new JLabel("→  " + text);
        l.setFont(FONT_TITLE);
        l.setForeground(ACCENT);
        c.add(l);

        c.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { c.setBackground(new Color(40, 50, 62)); }
            public void mouseExited(MouseEvent e)  { c.setBackground(BG_CARD); }
            public void mouseClicked(MouseEvent e) { cardLayout.show(cards, card); }
        });
        return c;
    }

    @FunctionalInterface interface StatSupplier { int get(); }

    // ─────────────────────────────────────────────────────────────────
    //  CUSTOMER PANEL
    // ─────────────────────────────────────────────────────────────────
    JPanel buildCustomerPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(sectionTitle("Customer Management"), BorderLayout.NORTH);

        JTabbedPane tabs = styledTabs();

        // ── View All ──
        JPanel viewTab = darkPanel(new BorderLayout(0, 8));
        String[] custCols = {"ID", "Name", "Phone", "Email", "Address", "PAN"};
        DefaultTableModel custModel = tableModel(custCols);
        JTable custTable = styledTable(custModel);
        JButton refreshBtn = actionBtn("↺  Refresh", ACCENT);
        refreshBtn.addActionListener(e -> {
            custModel.setRowCount(0);
            List<Customer> list = customerDAO.getAll();
            for (Customer c : list)
                custModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getPhone(), c.getEmail(), c.getAddress(), c.getPanNumber()});
            status("Loaded " + list.size() + " customer(s).");
        });
        viewTab.add(refreshBtn, BorderLayout.NORTH);
        viewTab.add(new JScrollPane(custTable), BorderLayout.CENTER);
        tabs.addTab("View All", viewTab);

        // ── Add ──
        JPanel addTab = darkPanel(new GridBagLayout());
        JTextField fCustId   = field("e.g. CUST06");
        JTextField fName     = field("Full Name");
        JTextField fPhone    = field("Phone Number");
        JTextField fEmail    = field("Email");
        JTextField fAddress  = field("Address");
        JTextField fPan      = field("e.g. ABCDE1234F");
        JButton    addBtn    = actionBtn("✚  Add Customer", ACCENT2);

        JPanel form = formGrid(
            new String[]{"Customer ID", "Name", "Phone", "Email", "Address", "PAN"},
            new JComponent[]{fCustId, fName, fPhone, fEmail, fAddress, fPan},
            addBtn
        );
        addTab.add(form);

        addBtn.addActionListener(e -> {
            try {
                String id  = fCustId.getText().trim().toUpperCase();
                String nm  = fName.getText().trim();
                String ph  = fPhone.getText().trim();
                String em  = fEmail.getText().trim();
                String ad  = fAddress.getText().trim();
                String pan = fPan.getText().trim().toUpperCase();
                if (id.isEmpty() || nm.isEmpty() || pan.isEmpty())
                    throw new FinancianException("ID, Name, and PAN cannot be empty.", 400);
                if (pan.length() != 10)
                    throw new FinancianException("PAN must be exactly 10 characters.", 400);
                customerDAO.add(new Customer(id, nm, ph, em, ad, pan));
                clearFields(fCustId, fName, fPhone, fEmail, fAddress, fPan);
                status("✔ Customer " + id + " added successfully.");
            } catch (FinancianException ex) {
                showErr("Validation Error", ex.getMessage());
            } catch (Exception ex) {
                showErr("Error", ex.getMessage());
            }
        });
        tabs.addTab("Add Customer", addTab);

        // ── Search ──
        JPanel searchTab = darkPanel(new BorderLayout(0, 8));
        JTextField searchField = field("Enter name to search...");
        JButton searchBtn = actionBtn("🔍  Search", ACCENT);
        DefaultTableModel searchModel = tableModel(custCols);
        JTable searchTable = styledTable(searchModel);

        JPanel searchTop = new JPanel(new BorderLayout(8, 0));
        searchTop.setOpaque(false);
        searchTop.add(searchField, BorderLayout.CENTER);
        searchTop.add(searchBtn,  BorderLayout.EAST);
        searchTab.add(searchTop, BorderLayout.NORTH);
        searchTab.add(new JScrollPane(searchTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            searchModel.setRowCount(0);
            List<Customer> res = customerDAO.searchByName(searchField.getText().trim());
            for (Customer c : res)
                searchModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getPhone(), c.getEmail(), c.getAddress(), c.getPanNumber()});
            status("Found " + res.size() + " result(s).");
        });
        tabs.addTab("Search", searchTab);

        // ── Delete ──
        JPanel delTab = darkPanel(new GridBagLayout());
        JTextField fDelId = field("e.g. CUST01");
        JButton delBtn = actionBtn("✖  Delete Customer", ACCENT_RED);
        JPanel delForm = formGrid(new String[]{"Customer ID"}, new JComponent[]{fDelId}, delBtn);
        delTab.add(delForm);
        delBtn.addActionListener(e -> {
            String id = fDelId.getText().trim().toUpperCase();
            if (id.isEmpty()) { showErr("Error", "Customer ID required."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer " + id + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                customerDAO.delete(id);
                fDelId.setText("");
                status("✔ Customer " + id + " deleted.");
            }
        });
        tabs.addTab("Delete", delTab);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────
    //  ACCOUNT PANEL
    // ─────────────────────────────────────────────────────────────────
    JPanel buildAccountPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(sectionTitle("Account Management"), BorderLayout.NORTH);

        JTabbedPane tabs = styledTabs();

        // View All
        String[] accCols = {"Account ID", "Type", "Balance (Rs.)", "Open Date", "Customer ID"};
        DefaultTableModel accModel = tableModel(accCols);
        JTable accTable = styledTable(accModel);
        JPanel viewTab = darkPanel(new BorderLayout(0, 8));
        JButton refBtn = actionBtn("↺  Refresh", ACCENT);
        refBtn.addActionListener(e -> {
            accModel.setRowCount(0);
            List<Account> list = accountDAO.getAll();
            for (Account a : list)
                accModel.addRow(new Object[]{a.getAccountId(), a.getAccountType(), a.getBalance(), a.getOpenDate(), a.getCustomerId()});
            status("Loaded " + list.size() + " account(s).");
        });
        viewTab.add(refBtn, BorderLayout.NORTH);
        viewTab.add(new JScrollPane(accTable), BorderLayout.CENTER);
        tabs.addTab("View All", viewTab);

        // View by Customer
        JPanel byCustomerTab = darkPanel(new BorderLayout(0, 8));
        JTextField fCid = field("e.g. CUST01");
        JButton findBtn = actionBtn("🔍  Find", ACCENT);
        DefaultTableModel byModel = tableModel(accCols);
        JTable byTable = styledTable(byModel);
        JPanel byTop = new JPanel(new BorderLayout(8, 0));
        byTop.setOpaque(false);
        byTop.add(labeled("Customer ID", fCid), BorderLayout.CENTER);
        byTop.add(findBtn, BorderLayout.EAST);
        byCustomerTab.add(byTop, BorderLayout.NORTH);
        byCustomerTab.add(new JScrollPane(byTable), BorderLayout.CENTER);
        findBtn.addActionListener(e -> {
            byModel.setRowCount(0);
            List<Account> list = accountDAO.getByCustomerId(fCid.getText().trim().toUpperCase());
            for (Account a : list)
                byModel.addRow(new Object[]{a.getAccountId(), a.getAccountType(), a.getBalance(), a.getOpenDate(), a.getCustomerId()});
            status("Found " + list.size() + " account(s).");
        });
        tabs.addTab("By Customer", byCustomerTab);

        // Add Account
        JPanel addTab = darkPanel(new GridBagLayout());
        JTextField fAccId   = field("e.g. ACC06");
        JTextField fCustId2 = field("e.g. CUST01");
        JComboBox<String> fType = styledCombo("Savings", "Current", "Salary");
        JTextField fBal     = field("Opening Balance in Rs.");
        JTextField fDate    = field("YYYY-MM-DD");
        JButton addBtn = actionBtn("✚  Add Account", ACCENT2);
        JPanel form = formGrid(
            new String[]{"Account ID", "Customer ID", "Account Type", "Opening Balance", "Open Date"},
            new JComponent[]{fAccId, fCustId2, fType, fBal, fDate},
            addBtn
        );
        addTab.add(form);
        addBtn.addActionListener(e -> {
            try {
                BigDecimal bal = new BigDecimal(fBal.getText().trim());
                Date d = Date.valueOf(fDate.getText().trim());
                accountDAO.add(new Account(
                    fAccId.getText().trim().toUpperCase(),
                    (String) fType.getSelectedItem(),
                    bal, d,
                    fCustId2.getText().trim().toUpperCase()
                ));
                clearFields(fAccId, fCustId2, fBal, fDate);
                status("✔ Account added.");
            } catch (Exception ex) { showErr("Error", ex.getMessage()); }
        });
        tabs.addTab("Add Account", addTab);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────
    //  LOAN PANEL
    // ─────────────────────────────────────────────────────────────────
    JPanel buildLoanPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(sectionTitle("Loan Management"), BorderLayout.NORTH);

        JTabbedPane tabs = styledTabs();

        // View All
        String[] loanCols = {"Loan ID", "Type", "Principal (Rs.)", "Rate (%)", "Tenure", "Start Date", "Account ID"};
        DefaultTableModel loanModel = tableModel(loanCols);
        JTable loanTable = styledTable(loanModel);
        JPanel viewTab = darkPanel(new BorderLayout(0, 8));
        JButton refBtn = actionBtn("↺  Refresh", ACCENT);
        refBtn.addActionListener(e -> {
            loanModel.setRowCount(0);
            List<Loan> list = loanDAO.getAll();
            for (Loan l : list)
                loanModel.addRow(new Object[]{l.getLoanId(), l.getLoanType(), l.getPrincipalAmount(), l.getInterestRate(), l.getLoanTenure(), l.getStartDate(), l.getAccountId()});
            status("Loaded " + list.size() + " loan(s).");
        });
        viewTab.add(refBtn, BorderLayout.NORTH);
        viewTab.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        tabs.addTab("View All", viewTab);

        // Add Loan
        JPanel addTab = darkPanel(new GridBagLayout());
        JTextField fLoanId   = field("e.g. LOAN06");
        JTextField fAccId    = field("e.g. ACC01");
        JComboBox<String> fType = styledCombo("Home", "Car", "Personal", "Education", "Business");
        JTextField fPrincipal = field("Amount in Rs.");
        JTextField fRate      = field("e.g. 8.5");
        JTextField fTenure    = field("Months e.g. 24");
        JTextField fDate      = field("YYYY-MM-DD");
        JButton addBtn = actionBtn("✚  Add Loan", ACCENT2);
        JPanel form = formGrid(
            new String[]{"Loan ID", "Account ID", "Loan Type", "Principal (Rs.)", "Interest Rate (%)", "Tenure (months)", "Start Date"},
            new JComponent[]{fLoanId, fAccId, fType, fPrincipal, fRate, fTenure, fDate},
            addBtn
        );
        addTab.add(form);
        addBtn.addActionListener(e -> {
            try {
                loanDAO.add(new Loan(
                    fLoanId.getText().trim().toUpperCase(),
                    (String) fType.getSelectedItem(),
                    new BigDecimal(fPrincipal.getText().trim()),
                    new BigDecimal(fRate.getText().trim()),
                    Integer.parseInt(fTenure.getText().trim()),
                    Date.valueOf(fDate.getText().trim()),
                    fAccId.getText().trim().toUpperCase()
                ));
                clearFields(fLoanId, fAccId, fPrincipal, fRate, fTenure, fDate);
                status("✔ Loan added.");
            } catch (Exception ex) { showErr("Error", ex.getMessage()); }
        });
        tabs.addTab("Add Loan", addTab);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────
    //  EMI PANEL
    // ─────────────────────────────────────────────────────────────────
    JPanel buildEMIPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(sectionTitle("EMI Management"), BorderLayout.NORTH);

        JTabbedPane tabs = styledTabs();

        // View by Loan
        String[] emiCols = {"EMI ID", "Amount (Rs.)", "Due Date", "Status", "Loan ID"};
        DefaultTableModel emiModel = tableModel(emiCols);
        JTable emiTable = styledTable(emiModel);
        JPanel viewTab = darkPanel(new BorderLayout(0, 8));
        JTextField fLoanSearch = field("e.g. LOAN01");
        JButton findBtn = actionBtn("🔍  Find", ACCENT);
        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);
        top.add(labeled("Loan ID", fLoanSearch), BorderLayout.CENTER);
        top.add(findBtn, BorderLayout.EAST);
        viewTab.add(top, BorderLayout.NORTH);
        viewTab.add(new JScrollPane(emiTable), BorderLayout.CENTER);
        findBtn.addActionListener(e -> {
            emiModel.setRowCount(0);
            List<EMI> list = emiDAO.getByLoanId(fLoanSearch.getText().trim().toUpperCase());
            for (EMI em : list)
                emiModel.addRow(new Object[]{em.getEmiId(), em.getEmiAmount(), em.getDueDate(), em.getPaidStatus(), em.getLoanId()});
            status("Loaded " + list.size() + " EMI(s).");
        });
        tabs.addTab("View by Loan", viewTab);

        // Add EMI
        JPanel addTab = darkPanel(new GridBagLayout());
        JTextField fEmiId  = field("e.g. EMI011");
        JTextField fLoanId = field("e.g. LOAN01");
        JTextField fAmt    = field("Amount in Rs.");
        JTextField fDate   = field("YYYY-MM-DD");
        JComboBox<String> fStatus = styledCombo("Paid", "Unpaid", "Overdue");
        JButton addBtn = actionBtn("✚  Add EMI", ACCENT2);
        addTab.add(formGrid(
            new String[]{"EMI ID", "Loan ID", "Amount (Rs.)", "Due Date", "Status"},
            new JComponent[]{fEmiId, fLoanId, fAmt, fDate, fStatus},
            addBtn
        ));
        addBtn.addActionListener(e -> {
            try {
                emiDAO.add(new EMI(
                    fEmiId.getText().trim().toUpperCase(),
                    new BigDecimal(fAmt.getText().trim()),
                    Date.valueOf(fDate.getText().trim()),
                    (String) fStatus.getSelectedItem(),
                    fLoanId.getText().trim().toUpperCase()
                ));
                clearFields(fEmiId, fLoanId, fAmt, fDate);
                status("✔ EMI added.");
            } catch (Exception ex) { showErr("Error", ex.getMessage()); }
        });
        tabs.addTab("Add EMI", addTab);

        // Mark as Paid
        JPanel markTab = darkPanel(new GridBagLayout());
        JTextField fMarkId = field("e.g. EMI001");
        JButton markBtn = actionBtn("✔  Mark as Paid", ACCENT2);
        markTab.add(formGrid(new String[]{"EMI ID"}, new JComponent[]{fMarkId}, markBtn));
        markBtn.addActionListener(e -> {
            String id = fMarkId.getText().trim().toUpperCase();
            if (id.isEmpty()) { showErr("Error", "EMI ID required."); return; }
            emiDAO.markEMIAsPaid(id);
            fMarkId.setText("");
            status("✔ EMI " + id + " marked as Paid.");
        });
        tabs.addTab("Mark as Paid", markTab);

        // Update Status
        JPanel updateTab = darkPanel(new GridBagLayout());
        JTextField fUpdId = field("e.g. EMI001");
        JComboBox<String> fNewStatus = styledCombo("Paid", "Unpaid", "Overdue");
        JButton updBtn = actionBtn("⟳  Update Status", ACCENT_WARN);
        updateTab.add(formGrid(
            new String[]{"EMI ID", "New Status"},
            new JComponent[]{fUpdId, fNewStatus},
            updBtn
        ));
        updBtn.addActionListener(e -> {
            String id = fUpdId.getText().trim().toUpperCase();
            EMI emi = emiDAO.getById(id);
            if (emi == null) { showErr("Error", "EMI not found: " + id); return; }
            emi.setPaidStatus((String) fNewStatus.getSelectedItem());
            emiDAO.update(emi);
            fUpdId.setText("");
            status("✔ EMI " + id + " status updated.");
        });
        tabs.addTab("Update Status", updateTab);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────
    //  REPORTS PANEL
    // ─────────────────────────────────────────────────────────────────
    JPanel buildReportsPanel() {
        JPanel p = darkPanel(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(sectionTitle("Reports & Analytics"), BorderLayout.NORTH);

        JTabbedPane tabs = styledTabs();

        // Loan EMI Summary (JOIN)
        JPanel loanEmiTab = darkPanel(new BorderLayout(0, 8));
        JTextArea loanEmiOut = outputArea();
        JButton loanEmiBtn = actionBtn("▶  Run Loan + EMI Summary", ACCENT);
        loanEmiBtn.addActionListener(e -> {
            loanEmiOut.setText("Loading...\n");
            SwingWorker<Void, Void> w = new SwingWorker<>() {
                protected Void doInBackground() {
                    // Redirect DAO output — we call the method and capture via a simple approach
                    // Since viewLoanEMISummary prints to stdout, we call it and note output goes to terminal
                    // For GUI we load the loans directly
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.format("%-10s %-12s %-15s %-8s %-8s %-12s%n",
                        "Loan ID","Type","Principal","Rate%","Tenure","Start Date"));
                    sb.append("-".repeat(70)).append("\n");
                    for (Loan l : loanDAO.getAll())
                        sb.append(String.format("%-10s %-12s %-15s %-8s %-8s %-12s%n",
                            l.getLoanId(), l.getLoanType(), l.getPrincipalAmount(),
                            l.getInterestRate(), l.getLoanTenure(), l.getStartDate()));
                    SwingUtilities.invokeLater(() -> loanEmiOut.setText(sb.toString()));
                    return null;
                }
            };
            w.execute();
            status("Ran Loan + EMI Summary.");
        });
        loanEmiTab.add(loanEmiBtn, BorderLayout.NORTH);
        loanEmiTab.add(new JScrollPane(loanEmiOut), BorderLayout.CENTER);
        tabs.addTab("Loan + EMI Summary", loanEmiTab);

        // Customer Loan Report (3-table JOIN)
        JPanel custLoanTab = darkPanel(new BorderLayout(0, 8));
        JTextArea custLoanOut = outputArea();
        JButton custLoanBtn = actionBtn("▶  Run Customer + Loan Report", ACCENT);
        custLoanBtn.addActionListener(e -> {
            custLoanOut.setText("Running 3-table JOIN report...\nOutput also printed to terminal.\n\n");
            customerDAO.viewCustomerLoanSummary(); // prints to stdout
            custLoanOut.append("Done. See terminal for full JOIN output.");
            status("Ran Customer Loan Report.");
        });
        custLoanTab.add(custLoanBtn, BorderLayout.NORTH);
        custLoanTab.add(new JScrollPane(custLoanOut), BorderLayout.CENTER);
        tabs.addTab("Customer Loan Report", custLoanTab);

        // EMI Risk Report
        JPanel riskTab = darkPanel(new BorderLayout(0, 8));
        JTextArea riskOut = outputArea();
        JButton riskBtn = actionBtn("▶  Run EMI Risk Profile", ACCENT_WARN);
        riskBtn.addActionListener(e -> {
            riskOut.setText("Running 5-table JOIN report...\nOutput also printed to terminal.\n\n");
            emiDAO.viewEMIRiskReport();
            riskOut.append("Done. See terminal for full risk report.");
            status("Ran EMI Risk Report.");
        });
        riskTab.add(riskBtn, BorderLayout.NORTH);
        riskTab.add(new JScrollPane(riskOut), BorderLayout.CENTER);
        tabs.addTab("EMI Risk Profile", riskTab);

        // Overdue EMI Stored Procedure
        JPanel overdueTab = darkPanel(new BorderLayout(0, 8));
        JTextField fOvLoanId = field("e.g. LOAN01");
        JButton ovBtn = actionBtn("▶  Run Stored Procedure", ACCENT_RED);
        JTextArea ovOut = outputArea();
        JPanel ovTop = new JPanel(new BorderLayout(8, 0));
        ovTop.setOpaque(false);
        ovTop.add(labeled("Loan ID", fOvLoanId), BorderLayout.CENTER);
        ovTop.add(ovBtn, BorderLayout.EAST);
        ovBtn.addActionListener(e -> {
            String loanId = fOvLoanId.getText().trim().toUpperCase();
            if (loanId.isEmpty()) { showErr("Error", "Loan ID required."); return; }
            ovOut.setText("Running stored procedure for Loan: " + loanId + "\nOutput printed to terminal.\n");
            emiDAO.callGetOverdueEMIs(loanId);
            ovOut.append("Done.");
            status("Ran Overdue EMI procedure for " + loanId);
        });
        overdueTab.add(ovTop, BorderLayout.NORTH);
        overdueTab.add(new JScrollPane(ovOut), BorderLayout.CENTER);
        tabs.addTab("Overdue EMI (Proc)", overdueTab);

        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────
    //  UI HELPERS
    // ─────────────────────────────────────────────────────────────────
    JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_DARK);
        return p;
    }

    JPanel card() {
        JPanel c = new JPanel();
        c.setBackground(BG_CARD);
        c.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        return c;
    }

    JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADER);
        l.setForeground(TEXT_MAIN);
        l.setBorder(new EmptyBorder(0, 0, 12, 0));
        return l;
    }

    JTabbedPane styledTabs() {
        JTabbedPane t = new JTabbedPane();
        t.setBackground(BG_PANEL);
        t.setForeground(TEXT_MAIN);
        t.setFont(FONT_LABEL);
        UIManager.put("TabbedPane.selected", BG_CARD);
        return t;
    }

    JTextField field(String placeholder) {
        JTextField tf = new JTextField(22);
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_MAIN);
        tf.setCaretColor(ACCENT);
        tf.setFont(FONT_LABEL);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR),
            new EmptyBorder(4, 8, 4, 8)
        ));
        // Placeholder
        tf.setText(placeholder);
        tf.setForeground(TEXT_MUTED);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_MAIN); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(TEXT_MUTED); }
            }
        });
        return tf;
    }

    JComboBox<String> styledCombo(String... options) {
        JComboBox<String> cb = new JComboBox<>(options);
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_MAIN);
        cb.setFont(FONT_LABEL);
        return cb;
    }

    JButton actionBtn(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFont(FONT_TITLE);
        b.setForeground(BG_DARK);
        b.setBackground(accent);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(accent.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(accent); }
        });
        return b;
    }

    JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_MAIN);
        t.setFont(FONT_SMALL);
        t.setGridColor(BORDER_CLR);
        t.setRowHeight(26);
        t.setSelectionBackground(new Color(50, 80, 120));
        t.setSelectionForeground(TEXT_MAIN);
        t.getTableHeader().setBackground(BG_PANEL);
        t.getTableHeader().setForeground(ACCENT);
        t.getTableHeader().setFont(FONT_TITLE);
        t.setShowVerticalLines(false);
        return t;
    }

    DefaultTableModel tableModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    JTextArea outputArea() {
        JTextArea ta = new JTextArea();
        ta.setBackground(new Color(13, 17, 23));
        ta.setForeground(ACCENT2);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setBorder(new EmptyBorder(8, 8, 8, 8));
        return ta;
    }

    JPanel labeled(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(4, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(label + ": ");
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        p.add(l, BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    JPanel formGrid(String[] labels, JComponent[] fields, JButton submitBtn) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel(labels[i] + " :");
            l.setFont(FONT_LABEL);
            l.setForeground(TEXT_MUTED);
            form.add(l, gc);

            gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
            form.add(fields[i], gc);
            gc.weightx = 0;
        }

        gc.gridx = 1; gc.gridy = labels.length; gc.anchor = GridBagConstraints.EAST;
        gc.insets = new Insets(16, 8, 6, 8);
        form.add(submitBtn, gc);
        return form;
    }

    void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    void showErr(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
        status("⚠ " + msg);
    }

    void status(String msg) {
        statusBar.setText("  " + msg);
    }

    // ─────────────────────────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(FinancianGUI::new);
    }
}
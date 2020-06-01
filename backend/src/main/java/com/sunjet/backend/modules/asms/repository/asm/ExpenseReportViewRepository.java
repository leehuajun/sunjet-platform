package com.sunjet.backend.modules.asms.repository.asm;


import com.sunjet.backend.modules.asms.entity.asm.view.ExpenseReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 费用速报 dao
 * Created by lhj on 16/9/17.
 */
public interface ExpenseReportViewRepository extends JpaRepository<ExpenseReportView, String>, JpaSpecificationExecutor<ExpenseReportView> {

}

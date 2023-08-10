package com.app.rcb.response

import androidx.room.Entity
import com.app.rcb.db.entity.*


data class StudentCenterResponse(
    var center_id: String,
    var center_name: String,
    var center_code: String
)

data class StudentProgram(
    var pm_id: String,
    var pm_center_id: String,
    var pm_program_title: String,
    var pm_description: String,
    var pm_code: String,
    var pm_printname: String
)


data class academicyear(var ay_id: String, var ay_name: String)

data class studentbranch(
    var bm_id: String,
    var bm_branchtitle: String,
    var bm_branchdescription: String
)

data class ddvalues(
    var dropdown_dd_id: Int,
    var dropdown_valueid: Int,
    var dropdown_name: String,
    var dropdown_status: String
)

data class academiccalendar(
    var acd_id: Int,
    var acd_calendar_id: Int,
    var acd_is_event: Int,
    var acd_event_id: Int,
    var acd_event_name: String,
    var acd_event_start_date: String,
    var acd_event_end_date: String,
    var acd_master_plan: String,
    var acd_status: String,
    var acd_created_by: String,
    var acd_created_at: String,
    var acd_updated_by: String,
    var acd_updated_at: String,
    var acd_marked_deleted: Int,
    var acd_deleted_by: Int,
    var acd_deleted_at: String
)

data class getdropdownvalue(var status: String, var message: String, var Data: ArrayList<Dropdown>)

data class getstudentbranch(var status: String, var message: String, var Branch: ArrayList<Branch>)

data class getacademicyear(var status: String, var message: String, var AcademicYear: ArrayList<Ay>)
//data class getholiday(var status: String, var message: String, var holiday: ArrayList<Holiday>)

data class getstudentprogram(
    var status: String,
    var message: String,
    var Programs: ArrayList<Program>
)

data class getstudentcenter(var status: String, var message: String, var centers: ArrayList<Center>)

data class getstudentcalendar(var status: Int, var ac: ArrayList<academiccalendar>)


data class getfeedetail(var status: Int, var Student_fee: ArrayList<studentfee>)

data class studentfee(
    var studentfeesch_id: Int,
    var studentfeesch_student_id: Int,
    var studentfeesch_fs_name: String,
    var studentfeesch_fs_print_name: String,
    var semester_name: String,
    var session_name: String,
    var studentfeesch_fs_scheme_type: Int,
    var fees_details: ArrayList<feedetail>
)

data class feedetail(
    var sfsd_id: Int,
    var sfsd_studentfeesch_id: Int,
    var sfsd_fsd_id: Int,
    var sfsd_fee_id: Int,
    var sfsd_fee_name: String,
    var sfsd_fee_print_name: String,
    var sfsd_fee_code: String,
    var sfsd_fee_priority: Int,
    var sfsd_fee_cat_id: String,
    var sfsd_fsd_amount: Int,
    var sfsd_due_date: String,
    var sfsd_is_discount: Int,
    var sfsd_discount_amount: Int,
    var sfsd_net_amount: Int,
    var sfsd_total_paid_amount: Int,
    var sfsd_total_refund_amount: Int,
    var sfsd_currency: Int,
    var sfsd_is_total_amount: Int,
    var sfsd_payment_status: Int,
    var fc_name: String,
    var feecurrency_name: String,
    var feecurrency_sign: String,
    var orderId: String,
    var customerId: Int,
    var checksum: Int
)

data class assignment(
    var id: Int,
    var center_id: Int,
    var program_id: Int,
    var branch_id: Int,
    var session_id: Int,
    var season_id: Int,
    var semester_id: Int,
    var course_id: Int,
    var sel_course_id:String,
    var course_name: String?,
    var course_code: String?,
    var student_id: String?,
    var as_name: String,
    var as_description: String,
    var assignments: String?,
    var as_points: String,
    var due_date: String,
    var is_submitted: String?,
    var status: Int,
    var added_area:Int,
    var add_by: Int,
    var created_at: String,
    var updated_at: String?,
    var ca_token:String,
    var cron_status:String,
    var cron_date:String?,
    var cas_id: Int,
    var cas_as_id: Int,
    var cas_student_id: Int,
    var cas_full_marks: Int,
    var cas_obtained_marks: String?,
    var cas_is_submmitted: Int,
    var cas_as_submitted_id: String?
)

data class getassignment(var status: Int, var assignment: ArrayList<assignment>)

data class downloadassignment(
    var id: Int,
    var center_id: Int,
    var program_id: Int,
    var branch_id: Int,
    var session_id: Int,
    var season_id: Int,
    var semester_id: Int,
    var course_id: Int,
    var course_name: String?,
    var course_code: String?,
    var student_id: String?,
    var as_name: String,
    var as_description: String,
    var assignments: String?,
    var as_points: Int,
    var due_date: String,
    var is_submitted: String?,
    var status: Int,
    var add_by: Int,
    var created_at: String,
    var updated_at: String?,
    var assignment_id: Int,
    var assigndocument_file: String
)

data class getdownloadassignment(
    var status: Int,
    var meesage: String,
    var assignment: ArrayList<downloadassignment>
)

/*data class objspecialization(var sm_id:Int,var sm_center_id:Int,var sm_program_id:Int,
var sm_title:String,var sm_description:String,var sm_status:Int)*/

data class getspecialization(
    var status: Int,
    var message: String,
    var Specialization: ArrayList<Specialization>
)


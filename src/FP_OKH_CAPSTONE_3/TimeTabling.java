/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FP_OKH_CAPSTONE_3;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author Or
 */
public class TimeTabling {
   static String folderDataset = "D:\\data e\\Kuliah\\Semester 6\\Optimasi Kombinatorik dan Heuristik\\Tugas\\FP\\";
    static String namafile[][] = {	{"car-f-92", "Carleton92"}, {"car-s-91", "Carleton91"}, {"ear-f-83", "EarlHaig83"}, {"hec-s-92", "EdHEC92"}, 
					{"kfu-s-93", "KingFahd93"}, {"lse-f-91", "LSE91"}, {"pur-s-93", "pur93"}, {"rye-s-93", "rye92"}, {"sta-f-83", "St.Andrews83"},
					{"tre-s-92", "Trent92"}, {"uta-s-92", "TorontoAS92"}, {"ute-s-92", "TorontoE92"}, {"yor-f-83", "YorkMills83"}
								};
    
    static int timeslot[]; // fill with course & its timeslot
    static int[][] conflict_matrix, course_sorted, hasil_timeslot;
	
	private static Scanner scanner;
	
    public static void main(String[] args) throws IOException {
        scanner = new Scanner(System.in);
        for	(int i=0; i< namafile.length; i++)
        	System.out.println(i+1 + ". Penjadwalan " + namafile[i][1]);
        
        System.out.print("\nSilahkan pilih file untuk dijadwalkan : ");
        int pilih = scanner.nextInt();
        
        String filePilihanInput = namafile[pilih-1][0];
        String filePilihanOutput = namafile[pilih-1][1];
        
        String file = folderDataset + filePilihanInput;
        
       	
        FP_OKH_CAPSTONE_2.Course course = new FP_OKH_CAPSTONE_2.Course(file);
        int jumlahexam = course.getJumlahCourse();
        
        conflict_matrix = course.getConflictMatrix();
        int jumlahmurid = course.getJumlahMurid();
        
		// sort exam by degree
		course_sorted = course.sortingByDegree(conflict_matrix, jumlahexam);
		
		// scheduling
		/*
		 * Scheduling by largest degree
		 */
		
		long starttimeLargestDegree = System.nanoTime();
		FP_OKH_CAPSTONE_2.Schedule schedule = new FP_OKH_CAPSTONE_2.Schedule(file, conflict_matrix, jumlahexam);
		timeslot = schedule.schedulingByDegree(course_sorted);
		int[][] timeslotByLargestDegree = schedule.getSchedule();
		long endtimeLargestDegree = System.nanoTime();
		
		/*
		 * params 1: file to be scheduling
		 * params 2: conflict matrix from file
		 * params 3: sort course by degree
		 * params 4: how many course from file
		 * params 5: how many student from file
		 * params 6: how many iterations
		 */
		FP_OKH_CAPSTONE_3.Optimization optimization = new FP_OKH_CAPSTONE_3.Optimization(file, conflict_matrix, course_sorted, jumlahexam, jumlahmurid, 100000);
		/*
		 * use hill climbing for timesloting
		 */
		
		
		/*
		 * use simmulated annealing for timesloting
		 * params : temperature
		 */
                long starttimeSA = System.nanoTime();
		optimization.getTimeslotBySimulatedAnnealing(100.0);
		long endtimeSA = System.nanoTime();
		
		/*
		 * use tabu search for timeslotting
		 */
		
		// end time
		System.out.println("PENJADWALAN UNTUK " + filePilihanOutput + "\n");
		
		System.out.println("Timeslot dibutuhkan (menggunakan \"Constructive Heuristics\") 	: " + schedule.getJumlahTimeSlot(schedule.getSchedule()));
		System.out.println("Penalti \"Constructive Heuristics\" 				: " + FP_OKH_CAPSTONE_2.Evaluator.getPenalty(conflict_matrix, schedule.getSchedule(), jumlahmurid));
		System.out.println("Waktu eksekusi yang dibutuhkan \"Constructive Heuristics\" " + ((double) (endtimeLargestDegree - starttimeLargestDegree)/1000000000) + " detik.\n");
		
		System.out.println("Timeslot dibutuhkan (menggunakan Simulated Annealing) 		: " + optimization.getJumlahTimeslotSimulatedAnnealing());
		System.out.println("Penalti Simulated Annealing 					: " + FP_OKH_CAPSTONE_2.Evaluator.getPenalty(conflict_matrix, optimization.getTimeslotSimulatedAnnealing(), jumlahmurid));
		System.out.println("Waktu eksekusi yang dibutuhkan Simmulated Annealing " + ((double) (endtimeSA - starttimeSA)/1000000000) + " detik.\n");
		
		
//		double[] penaltyList = optimization.getTabuSearchPenaltyList();
//		hasil_timeslot = optimization.getTimeslotHillClimbing();
//		hasil_timeslot = optimization.getTimeslotSimulatedAnnealing();
//		writePenaltyListFile(penaltyList, filePilihanOutput);
    }
    
    public static void writeSolFile(int[][] hasiltimeslot, String namaFileOutput) throws IOException {
    	String directoryOutput = "C:/Users/ZAP/Google Drive/KULIAH/OKH/Tugas/UAS/ExamTimetableEvaluation/" + namaFileOutput +".sol";
        FileWriter writer = new FileWriter(directoryOutput, true);
        for (int i = 0; i < hasiltimeslot.length; i++) {
            for (int j = 0; j < hasiltimeslot[i].length; j++) {
                  writer.write(hasiltimeslot[i][j]+ " ");
            }
            writer.write("\n");
        }
        writer.close();
        
		System.out.println("\nFile penjadwalan " + namaFileOutput+ " berhasil dibuat");
	}
    
    public static void writePenaltyListFile(double[] penaltyList, String namaFileOutput) throws IOException {
    	String directoryOutput = "C:/Users/ZAP/Google Drive/KULIAH/OKH/Tugas/UAS/" + namaFileOutput +".txt";
        FileWriter writer = new FileWriter(directoryOutput, true);
        
        for (int j = 0; j < penaltyList.length; j++) {
            writer.write(penaltyList[j]+ " ");
            writer.write("\n");
//        	System.out.println(penaltyList[j]);
        }
        writer.close();
        
		System.out.println("\nFile list penalty " + namaFileOutput+ " berhasil dibuat");
	} 
}

package kr.or.ddit.comm.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import javax.servlet.http.Part;

import kr.or.ddit.comm.dao.AtchFileDaoImpl;
import kr.or.ddit.comm.dao.IAtchFileDao;
import kr.or.ddit.comm.vo.AtchFileDetailVO;
import kr.or.ddit.comm.vo.AtchFileVO;

public class AtchFileServiceImpl implements IAtchFileService {

	private static final String UPLOAD_DIR = "upload_files";
	
	private static IAtchFileService fileService = new AtchFileServiceImpl();
	
	public static IAtchFileService getInstance() {
		return fileService;
	}
	
	private IAtchFileDao fileDao;
	
	public AtchFileServiceImpl() {
		fileDao = AtchFileDaoImpl.getInstance();
	}
	
	@Override
	public AtchFileVO saveAtchFileList(Collection<Part> parts) {

		// 업로드할 경로 지정/만들기
		String uploadPath = "d:/D_Other/upload_files";
		
		// 파일 경로를 넣은 파일 객체 생성자 만들기
		File uploadDir = new File(uploadPath);
		
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		AtchFileVO atchFileVO = null;
		
		boolean isFirstFile = true; // 첫번째 파일인지 여부 확인하는 용도
		
		for(Part part : parts) {
			String fileName = part.getSubmittedFileName(); // 업로드 파일명 추출하기
			
			// 파일이름이 null이 아니거나 파일이름이 무제면(파일을 넣지 않고 업로드 했을 때)
			// 즉, 파일을 정상적으로 업로드 했을 때
			if (fileName != null && !fileName.equals("")) {
				
				// 첫번째 업로드 파일인지 체크, isFirstFile 이 파일이 첫번째 파일이 맞으면(true)이면
				// 첫번째 업로드 파일인지 체크하는 이유 : 파일을 여러개 올렸을 때 첫번째만 Atch_File에 인서트하고, 두번째부터는 Atch_File_Detail에만 추가하려고
				if (isFirstFile) { 
					isFirstFile = false; // 확인 후에 첫번째 파일이 아님으로 바꿈
					
					atchFileVO = new AtchFileVO();
					
					fileDao.insertAtchFile(atchFileVO); // ATCH_FILE에 insert하기
				}
				
				long fileSize = part.getSize(); // 파일크기  
				String saveFileName = UUID.randomUUID().toString().replace("-", ""); // 저장파일명
				String saveFilePath = uploadPath + "/" + saveFileName; // 저장파일경로
				
				// 확장자 추출
				String fileExtension = fileName.lastIndexOf(".") < 0 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
				
				try {
					// 업로드를 할 파일 경로 저장하기
					part.write(saveFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// 이제 db에 인서트 작업 시작
				// VO에 정보 세팅하기
				AtchFileDetailVO atchFileDetailVO = new AtchFileDetailVO();
				// insertAtchFile()를 실행하고 마이바티스 xml에서 selectKey를 실행한 순간, atchFileId에 값이 세팅됨
				atchFileDetailVO.setAtchFileId(atchFileVO.getAtchFileId()); // 마이바티스가 xml에서 atchFileVO에 정보를 저장해줌, 가져오기
				atchFileDetailVO.setStreFileNm(saveFileName);
				atchFileDetailVO.setFileSize(fileSize);
				atchFileDetailVO.setOrignlFileNm(fileName);
				atchFileDetailVO.setFileStreCours(saveFilePath); // setStreFileNm : 파일 대상 경로
				atchFileDetailVO.setFileExtsn(fileExtension);
				
				// 다오 호출해서 인서트 하기
				fileDao.insertAtchFileDetail(atchFileDetailVO); // 파일 세부정보 저장
				
				try {
					// 임시 업로드 파일 삭제 처리하기
					// 임시파일 : 첨부파일 사이즈가 일정 이상이면 임시파일로 떨어짐 > 잔여부스러기가 남아 메모리가 지저분해짐 > delete : 청소하기
					part.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return atchFileVO;
	}

	@Override
	public AtchFileVO getAtchFile(AtchFileVO fileVO) {
		return fileDao.getAtchFile(fileVO);
	}

	@Override
	public AtchFileDetailVO getAtchFileDetail(AtchFileDetailVO atchFileDetailVO) {
		return fileDao.getAtchFileDetail(atchFileDetailVO);
	}
	
	
	// 파일 업로드 만든 것 DAO 잘 돌아가는지 최종 테스트 해보기
	private void daoTest() {
		boolean isFirstFile = true;
		
		AtchFileVO atchFileVO = null;
		
		if (isFirstFile) { 
			isFirstFile = false; // 확인 후에 첫번째 파일이 아님으로 바꿈
			
			atchFileVO = new AtchFileVO();
			
			fileDao.insertAtchFile(atchFileVO); // ATCH_FILE에 insert하기
		}
		
		String uploadPath = "d:/D_Other/upload_files";
		
		String fileName = "abc.jpg";
		
		long fileSize = 10000; // 파일크기
		String saveFileName = UUID.randomUUID().toString().replace("-", ""); // 저장파일명
		String saveFilePath = uploadPath + "/" + saveFileName; // 저장파일경로
		
		// 확장자 추출
		String fileExtension = fileName.lastIndexOf(".") < 0 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		
		// DB쪽만 체크할 것이기 때문에 필요없음, 지우기
//		try {
//			// 업로드 파일 저장하기
//			part.write(saveFilePath);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// 이제 db에 인서트 작업 시작
		// VO에 정보 세팅하기
		AtchFileDetailVO atchFileDetailVO = new AtchFileDetailVO();
		atchFileDetailVO.setAtchFileId(atchFileVO.getAtchFileId()); // 마이바티스가 xml에서 여기에 정보를 저장해줌
		atchFileDetailVO.setStreFileNm(saveFileName);
		atchFileDetailVO.setFileSize(fileSize);
		atchFileDetailVO.setOrignlFileNm(fileName);
		atchFileDetailVO.setFileStreCours(saveFilePath); // setStreFileNm : 파일 대상 경로
		atchFileDetailVO.setFileExtsn(fileExtension);
		
		// 다오 호출해서 인서트 하기
		fileDao.insertAtchFileDetail(atchFileDetailVO); // 파일 세부정보 저장
		
		System.out.println("성공!!");
	}
	
	public static void main(String[] args) {
//		// 확장자명 추출하기 (.jpg를 추출하는 코드)
//		String fileName = "aaa.jpg";
//		// .이 찍힌 마지막 인덱스의 값이 0보다 작으면 빈칸 반환, 같거나 크면 
//		String fileExt = fileName.lastIndexOf(".") < 0 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
//		
//		// 랜덤한 값이 아이디로 나오는 클래스의 랜덤 메소드를 사용해서 파일명으로 사용할 예정임
//		// .replace("-", "") : 랜덤 아이디의 -제거
//		// 7bf200c1-cd9d-4429-b1bf-9478ceb7d4ff -> a9290a5455af437c8d9102c36ba36c25
//		// 저장파일명 추출하기, saveFileName에서 사용(저장할 파일명)
//		System.out.println(UUID.randomUUID().toString().replace("-", "")); 
		
		
		// 파일 업로드 만든 것 DAO 잘 돌아가는지 최종 테스트 해보기
		new AtchFileServiceImpl().daoTest();
		
	}

}

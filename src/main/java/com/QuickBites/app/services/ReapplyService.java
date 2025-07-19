package com.QuickBites.app.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.DTO.AgentReapplyRequest;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.InvalidTokenException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.ReapplyToken;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.ReapplyTokenRepository;
import com.QuickBites.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ReapplyService {
	@Autowired
	private PendingUserRepository pendingUserRepo;

	@Autowired
	private ReapplyTokenRepository reapplyTokenRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ImageService imageService;

	@Autowired
	private FileService fileService;

	@Transactional
	public ReapplyToken validateToken(String token) {
		ReapplyToken reapplyToken = reapplyTokenRepository.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("This re-apply link is invalid or has expired."));

		if (reapplyToken.isUsed()) {
			throw new InvalidTokenException("This re-apply link has already been used.");
		}

		if (reapplyToken.getExpiresAt().isBefore(Instant.now())) {
			reapplyTokenRepository.delete(reapplyToken);
			throw new InvalidTokenException("This re-apply link has expired. Please contact support.");
		}

		if (!pendingUserRepo.existsById(reapplyToken.getPendingUserId())) {
			throw new InvalidTokenException("The application associated with this link no longer exists.");
		}

		return reapplyToken;
	}

	

	public PendingUser getPendingUserDetailsByToken(String token) {
		ReapplyToken validToken = this.validateToken(token);
		return pendingUserRepo.findById(validToken.getPendingUserId()).orElseThrow(() -> new ResourceNotFoundException(
				"Could not find the original application. Please contact support."));
	}

	
	@Transactional
	public void processReapplication(String token, AgentReapplyRequest request) {
		ReapplyToken reapplyToken = this.validateToken(token);
		PendingUser pendingUser = pendingUserRepo.findById(reapplyToken.getPendingUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Original application not found."));

		if (!pendingUser.getUserName().equalsIgnoreCase(request.getUserName())) {
			if (userRepo.existsByUserName(request.getUserName())
					|| pendingUserRepo.existsByUserName(request.getUserName())) {
				throw new ResourceAlreadyExistsException("Username '" + request.getUserName() + "' is already taken.");
			}
			pendingUser.setUserName(request.getUserName());
		}
		updateDocument(request.getCitizenshipPhotoFront(), pendingUser, "citizenshipPhotoFront");
		updateDocument(request.getCitizenshipPhotoBack(), pendingUser, "citizenshipPhotoBack");
		updateDocument(request.getDrivingLicense(), pendingUser, "drivingLicense");

		reapplyToken.setUsed(true);
		reapplyTokenRepository.save(reapplyToken);

		pendingUserRepo.save(pendingUser);

	}

	private void updateDocument(MultipartFile newFile, PendingUser user, String documentType) {
		if (newFile == null || newFile.isEmpty()) {
			return;
		}

		try {
			String oldFileName = null;
			switch (documentType) {
			case "citizenshipPhotoFront":
				oldFileName = user.getCitizenshipPhotoFront();
				break;
			case "citizenshipPhotoBack":
				oldFileName = user.getCitizenshipPhotoBack();
				break;
			case "drivingLicense":
				oldFileName = user.getDriverLicense();
				break;
			}
			String newFileName = fileService.saveFile(newFile);

			switch (documentType) {
			case "citizenshipPhotoFront":
				user.setCitizenshipPhotoFront(newFileName);
				break;
			case "citizenshipPhotoBack":
				user.setCitizenshipPhotoBack(newFileName);
				break;
			case "drivingLicense":
				user.setDriverLicense(newFileName);
				break;
			}

			if (oldFileName != null && !oldFileName.isEmpty()) {
				imageService.deleteImage(oldFileName, ImageType.DELIVERYAGENTREQUEST);
			}
		} catch (Exception e) {
			throw new FileStorageException("Could not update document: " + documentType, e);
		}
	}

}
